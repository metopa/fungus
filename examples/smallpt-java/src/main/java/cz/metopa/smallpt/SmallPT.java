package cz.metopa.smallpt;

import static java.lang.Math.pow;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public final class SmallPT {
    private static final class Vec {
        public double x, y, z;

        public Vec(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vec() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        @Override
        public String toString() {
            return "Vec[" + x + ", " + y + ", " + z + ']';
        }

        public Vec add(Vec other) { return new Vec(x + other.x, y + other.y, z + other.z); }

        public Vec sub(Vec other) { return new Vec(x - other.x, y - other.y, z - other.z); }

        public Vec mult(Vec other) { return new Vec(x * other.x, y * other.y, z * other.z); }

        public Vec mult(double other) { return new Vec(x * other, y * other, z * other); }

        public Vec norm() { return new Vec(x, y, z).mult(1 / Math.sqrt(x * x + y * y + z * z)); }

        public double dot(Vec other) { return x * other.x + y * other.y + z * other.z; }

        public Vec cross(Vec other) {
            return new Vec(y * other.z - z * other.y, z * other.x - x * other.z,
                           x * other.y - y * other.x);
        }
    }

    private static final class Ray {
        public Vec origin, dir;

        public Ray(Vec origin, Vec dir) {
            this.origin = origin;
            this.dir = dir;
        }

        @Override
        public String toString() {
            return "Ray{"
                + "origin=" + origin + ", dir=" + dir + '}';
        }
    }

    private enum ReflectionType { DIFF, SPEC, REFRACTION }

    private static class Sphere {
        public double rad;
        public Vec position, emission, color;
        ReflectionType reflectionType;

        public Sphere(double rad, Vec position, Vec emission, Vec color,
                      ReflectionType reflectionType) {
            this.rad = rad;
            this.position = position;
            this.emission = emission;
            this.color = color;
            this.reflectionType = reflectionType;
        }

        // returns distance, 0 if no hit
        public double intersect(Ray r) {
            // Solve t^2*d.d + 2*t*(o-p).d + (o-p).(o-p)-R^2 = 0
            Vec op = position.sub(r.origin);

            double eps = 1e-4;
            double b = op.dot(r.dir);
            double det = b * b - op.dot(op) + rad * rad;
            if (det < 0) {
                return 0;
            } else {
                det = Math.sqrt(det);
            }
            double t = b - det;
            if (t > eps) {
                return t;
            }
            t = b + det;

            return t > eps ? t : 0;
        }
    }

    private static final class IntersectResult {
        public double t;
        public Sphere sphere;

        public IntersectResult(double t, Sphere sphere) {
            this.t = t;
            this.sphere = sphere;
        }
    }

    // clang-format off
    private Sphere[] spheres = {new Sphere(1e5, new Vec(1e5 + 1, 40.8, 81.6), new Vec(),
            new Vec(.75, .25, .25), ReflectionType.DIFF), // Left
            new Sphere(1e5, new Vec(-1e5 + 99, 40.8, 81.6), new Vec(), new Vec(.25, .25, .75),
                    ReflectionType.DIFF), // Right
            new Sphere(1e5, new Vec(50, 40.8, 1e5), new Vec(), new Vec(.75, .75, .75),
                    ReflectionType.DIFF), // Back
            new Sphere(1e5, new Vec(50, 40.8, -1e5 + 170), new Vec(), new Vec(),
                    ReflectionType.DIFF), // Front
            new Sphere(1e5, new Vec(50, 1e5, 81.6), new Vec(), new Vec(.75, .75, .75),
                    ReflectionType.DIFF), // Bottom
            new Sphere(1e5, new Vec(50, -1e5 + 81.6, 81.6), new Vec(), new Vec(.75, .75, .75),
                    ReflectionType.DIFF), // Top
            new Sphere(16.5, new Vec(27, 16.5, 47), new Vec(), new Vec(.999, .999, .999),
                    ReflectionType.SPEC), // Mirror
            new Sphere(16.5, new Vec(73, 16.5, 78), new Vec(), new Vec(.999, .999, .999),
                    ReflectionType.REFRACTION), // Glass
            new Sphere(600, new Vec(50, 681.6 - .27, 81.6), new Vec(12, 12, 12), new Vec(),
                    ReflectionType.DIFF) // Light
    };
    // clang-format on

    static double clamp(double v) { return v < 0 ? 0 : v > 1 ? 1 : v; }

    static int gammaAdjustedRgb(double f) { return (int)(pow(clamp(f), 1 / 2.2) * 255 + .5); }

    IntersectResult intersect(Ray r) {
        double t = 1e20;
        Sphere sphere = null;
        for (int i = spheres.length - 1; i >= 0; i--) {
            double d = spheres[i].intersect(r);
            if (d > 0 && d < t) {
                t = d;
                sphere = spheres[i];
            }
        }
        return sphere != null ? new IntersectResult(t, sphere) : null;
    }

    Vec radiance(Ray r, int depth) {
        IntersectResult intersection = intersect(r);
        if (intersection == null) {
            return new Vec(); // if miss, return black
        }

        Vec x = r.origin.add(r.dir.mult(intersection.t));
        Vec n = (x.sub(intersection.sphere.position)).norm();
        Vec nl = n.dot(r.dir) < 0 ? n : n.mult(-1);
        Vec f = intersection.sphere.color;

        double p = Math.max(f.x, Math.max(f.y, f.z));
        if (++depth > 5) {
            if (Math.random() < p) {
                f = f.mult(1 / p);
            } else {
                return intersection.sphere.emission;
            }
        }

        if (intersection.sphere.reflectionType == ReflectionType.DIFF) {
            return calculateDiffuseReflection(intersection.sphere, r, depth, x, n, nl, f);
        } else if (intersection.sphere.reflectionType == ReflectionType.SPEC) {
            return calculateSpecularReflection(intersection.sphere, r, depth, x, n, nl, f);
        } else {
            return calculateRefraction(intersection.sphere, r, depth, x, n, nl, f);
        }
    }

    private Vec calculateRefraction(Sphere sphere, Ray r, int depth, Vec x, Vec n, Vec nl, Vec f) {
        Ray reflectionRay = new Ray(x, r.dir.sub(n.mult(2 * n.dot(r.dir))));
        boolean into = n.dot(nl) > 0; // Ray from outside going in?
        double nc = 1;
        double nt = 1.5;
        double nnt = into ? nc / nt : nt / nc;
        double ddn = r.dir.dot(nl);
        double cos2t = 1 - nnt * nnt * (1 - ddn * ddn);

        if (cos2t < 0) { // Total internal reflection
            return sphere.emission.add(f.mult(radiance(reflectionRay, depth)));
        }

        double factor = (into ? 1 : -1) * (ddn * nnt + Math.sqrt(cos2t));
        Vec tDir = r.dir.mult(nnt).sub(n.mult(factor)).norm();

        double a = nt - nc;
        double b = nt + nc;
        double R0 = a * a / (b * b);
        double c = 1 - (into ? -ddn : tDir.dot(n));
        double Re = R0 + (1 - R0) * c * c * c * c * c;
        double Tr = 1 - Re;
        double P = .25 + .5 * Re;
        double RP = Re / P;
        double TP = Tr / (1 - P);
        if (depth > 2) {
            // Russian roulette
            return sphere.emission.add(f.mult(Math.random() < P
                                                  ? radiance(reflectionRay, depth).mult(RP)
                                                  : radiance(new Ray(x, tDir), depth).mult(TP)));
        } else {
            return sphere.emission.add(
                f.mult(radiance(reflectionRay, depth)
                           .mult(Re)
                           .add(radiance(new Ray(x, tDir), depth).mult(Tr))));
        }
    }

    private Vec calculateSpecularReflection(Sphere sphere, Ray r, int depth, Vec x, Vec n, Vec nl,
                                            Vec f) {
        return sphere.emission.add(
            f.mult(radiance(new Ray(x, r.dir.sub(n.mult(2 * n.dot(r.dir)))), depth)));
    }

    private Vec calculateDiffuseReflection(Sphere sphere, Ray r, int depth, Vec x, Vec n, Vec nl,
                                           Vec f) {
        double r1 = 2 * Math.PI * Math.random();
        double r2 = Math.random();
        double r2s = Math.sqrt(r2);

        Vec w = nl;
        Vec u = ((Math.abs(w.x) > .1 ? new Vec(0, 1, 0) : new Vec(1, 0, 0)).cross(w)).norm();
        Vec v = w.cross(u);
        Vec d = u.mult(Math.cos(r1) * r2s)
                    .add(v.mult(Math.sin(r1) * r2s))
                    .add(w.mult(Math.sqrt(1 - r2)));
        return sphere.emission.add(f.mult(radiance(new Ray(x, d.norm()), depth)));
    }

    public static void main(String[] args) {
        int w = 1024;
        int h = 768;
        int samplesPerPixel = args.length >= 1 ? Integer.parseInt(args[0]) / 4 : 1;
        Vec[] canvas = new Vec[w * h];
        for (int i = 0; i < canvas.length; i++) {
            canvas[i] = new Vec();
        }

        SmallPT renderer = new SmallPT();
        renderer.render(w, h, samplesPerPixel, canvas);

        try (PrintStream out = new PrintStream(new FileOutputStream("java_image.ppm"))) {
            out.printf("P3\n%d %d\n%d\n", w, h, 255);
            for (int i = 0; i < w * h; i++) {
                out.printf("%d %d %d ", gammaAdjustedRgb(canvas[i].x),
                           gammaAdjustedRgb(canvas[i].y), gammaAdjustedRgb(canvas[i].z));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String samplesThroughput(long sampleCount, long timeNs) {
        return String.format("%5.2fK samples/s", (double)sampleCount / timeNs * 1000000);
    }

    public void render(int w, int h, int samplesPerPixel, Vec[] canvas) {
        Ray cam = new Ray(new Vec(50, 52, 295.6), new Vec(0, -0.042612, -1).norm());
        Vec cx = new Vec(w * .5135 / h, 0, 0);
        Vec cy = cx.cross(cam.dir).norm().mult(.5135);
        long startTime = System.nanoTime();
        // long startSinceLastLog = startTime;
        // long samplesSinceLastLog = 0;
        for (int y = 0; y < h; y++) {
            // Loop over image rows
            System.out.printf("\rRendering (%d spp) %5.2f%%", samplesPerPixel * 4,
                              100. * y / (h - 1));
            /*if ((y + 1) % (5000 / samplesPerPixel) == 0) {
                long currentTime = System.nanoTime();
                System.out.printf(" [%s]\n", samplesThroughput(samplesSinceLastLog,
                                                               currentTime - startSinceLastLog));
                startSinceLastLog = currentTime;
                samplesSinceLastLog = 0;
            }*/

            for (int x = 0; x < w; x++) {
                // Loop cols
                for (int sy = 0, i = (h - y - 1) * w + x; sy < 2; sy++)
                    // 2x2 subpixel rows
                    for (int sx = 0; sx < 2; sx++) {
                        Vec r = new Vec();
                        // 2x2 subpixel cols
                        for (int s = 0; s < samplesPerPixel; s++) {
                            double r1 = 2 * Math.random();
                            double dx = r1 < 1 ? Math.sqrt(r1) - 1 : 1 - Math.sqrt(2 - r1);
                            double r2 = 2 * Math.random();
                            double dy = r2 < 1 ? Math.sqrt(r2) - 1 : 1 - Math.sqrt(2 - r2);
                            Vec d = cx.mult(((sx + .5 + dx) / 2 + x) / w - .5)
                                        .add(cy.mult(((sy + .5 + dy) / 2 + y) / h - .5))
                                        .add(cam.dir);
                            try {
                                r = r.add(
                                    radiance(new Ray(cam.origin.add(d.mult(140)), d.norm()), 0)
                                        .mult(1. / samplesPerPixel));
                            } catch (StackOverflowError ignored) {
                            }
                        } // Camera rays are pushed ^^^^^ forward to start in interior
                        canvas[i] =
                            canvas[i].add(new Vec(clamp(r.x), clamp(r.y), clamp(r.z)).mult(.25));
                    }
            }
            // samplesSinceLastLog += (long)w * samplesPerPixel * 4;
        }

        long duration = System.nanoTime() - startTime;
        long totalSamples = (long)samplesPerPixel * 4 * w * h;

        System.out.printf("\nRendered %d samples in %7.3f seconds [%s]\n", totalSamples,
                          duration / 1000000000., samplesThroughput(totalSamples, duration));
    }
}
