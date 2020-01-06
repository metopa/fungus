// Small Path Tracer

struct Ray {
    origin, dir
}

struct Sphere {
    rad, position, emission, color, refl_type
}

struct IntersectResult {
    t, sphere
}

func @dot(a, b) {
    return a.x * b.x + a.y * b.y + a.z * b.z;
}

func @cross(a, b) {
    return vec3(a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x);
}

func norm(v) {
    return v * (1 / sqrt(v @dot v));
}

func getScene() {
    var sc[9];
    //             rad   position                     emission          color                   refl_type
    sc[0] = Sphere(1e5,  vec3(1e5 + 1, 40.8, 81.6),   vec3(),           vec3(.75, .25, .25),    "DIFF"); // Left
    sc[1] = Sphere(1e5,  vec3(-1e5 + 99, 40.8, 81.6), vec3(),           vec3(.25, .25, .75),    "DIFF"); // Right
    sc[2] = Sphere(1e5,  vec3(50, 40.8, 1e5),         vec3(),           vec3(.75, .75, .75),    "DIFF"); // Back
    sc[3] = Sphere(1e5,  vec3(50, 40.8, -1e5 + 170),  vec3(),           vec3(),                 "DIFF"); // Front
    sc[4] = Sphere(1e5,  vec3(50, 1e5, 81.6),         vec3(),           vec3(.75, .75, .75),    "DIFF"); // Bottom
    sc[5] = Sphere(1e5,  vec3(50, -1e5 + 81.6, 81.6), vec3(),           vec3(.75, .75, .75),    "DIFF"); // Top
    sc[6] = Sphere(16.5, vec3(27, 16.5, 47),          vec3(),           vec3(.999, .999, .999), "SPEC"); // Mirror
    sc[7] = Sphere(16.5, vec3(73, 16.5, 78),          vec3(),           vec3(.999, .999, .999), "REFR"); // Glass
    sc[8] = Sphere(600,  vec3(50, 681.6 - .27, 81.6), vec3(12, 12, 12), vec3(),                 "DIFF"); // Light

    return sc;
}

func clamp(f) {
    return max(0, min(1, f));
}

func gammaAdjustedRgb(f) {
    return int(clamp(f) ^ (1 / 2.2) * 255 + .5);
}


// Returns distance, 0 if no hit
func @intersect(sphere, ray) {
    // Solve t^2*d.d + 2*t*(o-p).d + (o-p).(o-p)-R^2 = 0
    var op = sphere.position - ray.origin;

    var eps = 1e-4;
    var b = op @dot ray.dir;
    var det = b * b - op @dot op + sphere.rad * sphere.rad;
    if (det < 0) {
      return 0.;
    }

    det = sqrt(det);
    var t = b - det;
    if (t > eps) {
      return t;
    }
    t = b + det;

    if (t > eps) {
      return t;
    } else {
      return 0;
    }
}

func shootRay(scene, ray) {
    var t = 1e20; // infinity
    var sphere = null;

    for (var i = len(scene) - 1; i >= 0; i = i - 1) {
         var d = scene[i] @intersect ray;
         if (d > 0 && d < t) {
             t = d;
             sphere = scene[i];
         }
    }
    if (sphere != null) {
        return IntersectResult(t, sphere);
    } else {
        return null;
    }
}
/*
func radiance(scene, ray, depth) {
    var intersection = shootRay(scene, ray);
    if (intersection == null) {
        return vec3(); // if miss, return black
    }
    var x = ray.origin + ray.dir * intersection.t;
    var n = norm(x - intersection.sphere.position);

    var nl;
    if (n @dot ray.dir < 0) {
        nl = n;
    } else {
        nl = -n;
    }

    var f = intersection.sphere.color;
    var p = max(f.x, max(f.y, f.z));
    depth = depth + 1;
    if (depth > 5) {
        if (random() < p) {
            f = f * (1 / p);
        } else {
            return intersection.sphere.emission;
        }
    }
    if (intersection.sphere.refl_type == "DIFF") {
        return calculateDiffuseReflection(scene, intersection.sphere, ray, depth, x, n, nl, f);
    } else if (intersection.sphere.refl_type == "SPEC") {
        return calculateSpecularReflection(scene, intersection.sphere, ray, depth, x, n, nl, f);
    } else {
        return calculateRefraction(scene, intersection.sphere, ray, depth, x, n, nl, f);
    }
}
*/
func calculateRefraction(scene, sphere, ray, depth, x, n, nl, f) {
    var reflectionRay = Ray(x, ray.dir - 2 * n * n @dot ray.dir);
    var into = n @dot nl > 0; // Ray from outside going in?
    var nc = 1;
    var nt = 1.5;

    var nnt = 0.;
    if (into) {
        nnt = nc / nt;
    } else {
        nnt = nt / nc;
    }

    var ddn = ray.dir @dot nl;
    var cos2t = 1 - nnt * nnt * (1 - ddn * ddn);

    if (cos2t < 0) { // Total internal reflection
        return sphere.emission + f * radiance(scene, reflectionRay, depth);
    }
    var factor = dnn * nnt + sqrt(cos2t);
    if (!into) {
        factor = -factor;
    }

    var tDir = norm(ray.dir * nnt - n * factor);

    var a = nt - nc;
    var b = nt + nc;
    var R0 = a * a / (b * b);
    var c = 0.;
    if (into) {
        c = 1 + dnn;
    } else {
        c = 1 - tDir @dot n;
    }

    var Re = R0 + (1 - R0) * c * c * c * c * c;
    var Tr = 1 - Re;
    var P = .25 + .5 * Re;
    var RP = Re / P;
    var TP = Tr / (1 - P);

    if (depth > 2) {
        // Russian roulette
        if (random() < P) {
            return sphere.emission + f * radiance(scene, reflectionRay, depth) * RP;
        } else {
            return sphere.emission + f * radiance(scene, Ray(x, tDir), depth) * TP;
        }
    } else {
        return sphere.emission + f * (radiance(scene, reflectionRay, depth) * Re +
                                      radiance(scene, Ray(x, tDir),  depth) * Tr);
    }
}


func calculateSpecularReflection(scene, sphere, ray, depth, x, n, nl, f) {
     return sphere.emission + f * radiance(scene, Ray(x, ray.dir - 2 * n * n @dot ray.dir), depth);
 }


func calculateDiffuseReflection(scene, sphere, ray, depth, x, n, nl, f) {
    var PI = 3.1415926535;
    var r1 = 2 * PI * random();
    var r2 = random();
    var r2s = sqrt(r2);

    var w = nl;
    var u;
    if (abs(w.x) > .1) {
        u = vec3(0, 1, 0);
    } else {
        u = vec3(1, 0, 0);
    }

    var u = norm(u @cross w);
    var v = w @cross u;
    var d = u * (cos(r1) * r2s) + v * (sin(r1) * r2s) + w * sqrt(1 - r2);
    return sphere.emission + f * radiance(scene, Ray(x, norm(d), depth);
}

func main() {
    unit_tests();

    var w = 1024;
    var h = 768;
    var samplesPerPixel = 48 / 4;
    var outputFile = "fungus_image.ppm";
    var canvas[w][h] = vec3(0);


    render(canvas, samplesPerPixel);
}

func unit_tests() {
    assert(norm(vec3(-4, 0, 3)) == vec3(-0.8, 0, 0.6));
    assert(vec3(3, -3, 1) @cross vec3(4, 9, 2) == vec3(-15, -2, 39));

    var s = Sphere(2, vec3(5, 0, 0), null, null, null);
    assert(s @intersect Ray(vec3(0, 1, 0), vec3(1, 1, 0)) == 0);
    assert(s @intersect Ray(vec3(0, 0, 0), vec3(1, 0, 0)) == 3);
    assert(s @intersect Ray(vec3(1, 0, 0), vec3(1, 0, 0)) < 3);
    assert(s @intersect Ray(vec3(1, 0, 0), vec3(1, 0, 0)) > 0);

    assert(getScene()[1].color == vec3(.25, .25, .75));

    assert(gammaAdjustedRgb(0) == 0);
    assert(gammaAdjustedRgb(0.3) == 148);
    assert(gammaAdjustedRgb(1) == 255);
}
