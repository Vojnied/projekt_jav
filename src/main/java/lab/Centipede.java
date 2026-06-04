package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;
import java.util.ListIterator;

public class Centipede implements DrawableSimulable, Collisionable {

    private final World world;
    private final LinkedList<CentipedeSegment> segments = new LinkedList<>();
    private int dirX = 1; // 1 = doprava, -1 = doleva
    private double accumulator = 0;
    private double stepTime;
    private static final int CELL = CentipedeSegment.CELL;

    public Centipede(World world, Point2D start, int length, double stepTime) {
        this.world = world;
        this.stepTime = stepTime;

        for (int i = 0; i < length; i++) {
            SegmentType type = (i == 0) ? SegmentType.HEAD :
                (i == length - 1) ? SegmentType.TAIL :
                    SegmentType.BODY;

            CentipedeSegment seg = new CentipedeSegment(world,
                start.add(-i * CELL, 0),
                type,
                this);
            segments.add(seg);
            world.add(seg);
        }
    }

    public Centipede(World world, LinkedList<CentipedeSegment> segments, double stepTime) {
        this.world = world;
        this.segments.addAll(segments);
        this.stepTime = stepTime;

        for (CentipedeSegment s : segments) {
            s.setController(this);
            world.add(s);
        }

        updateTypes();
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (CentipedeSegment seg : segments) {
            seg.draw(gc);
        }
    }

    @Override
    public void simulate(double deltaTime) {
        if (segments.isEmpty()) return;

        accumulator += deltaTime;
        while (accumulator >= stepTime) {
            accumulator -= stepTime;
            step();
        }
    }

    private void step() {
        CentipedeSegment head = segments.getFirst();
        Point2D headPos = head.getPosition();

        Point2D next = headPos.add(dirX * CELL, 0);

        if (hitsWall(next) || hitsObstacle(next)) {
            dirX *= -1;
            next = headPos.add(0, CELL);
        }

        //wrap outside map
        if (next.getY() >= world.getHeight()) {
            next = new Point2D(next.getX(),0);
        }
        Point2D prev = headPos;
        head.setPosition(next);

        ListIterator<CentipedeSegment> it = segments.listIterator(1);
        while (it.hasNext()) {
            CentipedeSegment seg = it.next();
            Point2D tmp = seg.getPosition();
            seg.setPosition(prev);
            prev = tmp;
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        if (segments.isEmpty()) return new Rectangle2D(0,0,0,0);

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (CentipedeSegment s : segments) {
            Rectangle2D box = s.getBoundingBox();
            minX = Math.min(minX, box.getMinX());
            minY = Math.min(minY, box.getMinY());
            maxX = Math.max(maxX, box.getMaxX());
            maxY = Math.max(maxY, box.getMaxY());
        }

        return new Rectangle2D(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean intersect(Collisionable another) {
        return getBoundingBox().intersects(another.getBoundingBox());
    }

    @Override
    public void hitBy(Collisionable another) {
        // broad phase
        if (!intersect(another)) return;

        // narrow phase segemnts
        for (CentipedeSegment seg : segments) {
            if (seg.intersect(another)) {
                if (another instanceof Bullet) handleSegmentHit(seg);
                else if (another instanceof Cannon) {
                    world.firePlayerHit();
                }

                break;
            }
        }
    }

    public int getDirX() {
        return dirX;
    }

    private void handleSegmentHit(CentipedeSegment hit) {
        this.accumulator = 0;
        int index = segments.indexOf(hit);
        if (index == -1) return;

        world.add(new Obstacle(world, hit.getPosition()));

        world.remove(hit);
        segments.remove(hit);

        // head
        if (index == 0) {
            if (!segments.isEmpty()) {

                //last step
                for (CentipedeSegment s : segments) {
                    s.setPosition(s.getPosition().add(dirX * CELL, 0));
                }

                //new head goes down
                CentipedeSegment newHead = segments.getFirst();
                newHead.setPosition(newHead.getPosition().add(0, CELL));

                //change dir
                dirX *= -1;

            } else {
                world.removeCentipede(this);
                return;
            }
        }

        // body
        else {
            LinkedList<CentipedeSegment> tail = new LinkedList<>();

            while (segments.size() > index) {
                tail.addFirst(segments.removeLast());
            }

            if (!tail.isEmpty()) {

                // separate and step
                for (CentipedeSegment s : tail) {
                    s.setPosition(s.getPosition().add(dirX * CELL, 0));
                }

                // new head goes down
                CentipedeSegment newHead = tail.getFirst();
                newHead.setPosition(newHead.getPosition().add(0, CELL));

                // new centipede from rest
                Centipede newC = new Centipede(world, tail, this.stepTime);
                newC.setDirection(-dirX);
                world.addCentipede(newC);
            }
        }
        updateTypes();
    }


    private boolean hitsWall(Point2D pos) {
        return pos.getX() < 0 || pos.getX() + CELL > world.getWidth();
    }

    private boolean hitsObstacle(Point2D pos) {
        Rectangle2D box = new Rectangle2D(pos.getX(), pos.getY(), CELL, CELL);
        for (DrawableSimulable e : world.getEntities()) {
            if (e instanceof Obstacle o) {
                if (box.intersects(o.getBoundingBox())) return true;
            }
        }
        return false;
    }

    private void updateTypes() {
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).setType(
                i == 0 ? SegmentType.HEAD :
                    i == segments.size() - 1 ? SegmentType.TAIL : SegmentType.BODY
            );
        }
    }

    public void setDirection(int dirX) {
        this.dirX = dirX;
    }
    public void setStepTime(double stepTime) {
        this.stepTime = stepTime;
    }

    public double getStepTime() {
        return stepTime;
    }
}
