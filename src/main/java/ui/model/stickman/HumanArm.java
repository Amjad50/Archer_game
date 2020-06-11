package ui.model.stickman;

import utils.Vector;

public class HumanArm extends Arm {

    public HumanArm(double length) {
        super.addArm(length);
        super.addArm(length);
    }

    @Override
    public void follow(Vector target) {
        super.follow(target);
        correctAngle();
    }

    @Override
    public void addArm(double length) {
        throw new IllegalCallerException("Human arm can only have two segments");
    }

    private boolean isWrongAngle() {
        assert segments.size() == 2;

        ArmLegSegment first = segments.get(0);
        ArmLegSegment last = segments.get(1);

        Vector firstVec = first.getEnd().sub(first.getStart()).flipY();
        Vector lastVec = last.getEnd().sub(last.getStart()).flipY();

        double firstAngle = firstVec.angleDeg();
        double lastAngle = lastVec.angleDeg();

        if (firstAngle > 180)
            firstAngle -= 360;

        if (lastAngle > 180)
            lastAngle -= 360;

        return firstAngle > lastAngle;
    }

    private void correctAngle() {
        assert segments.size() == 2;
        if (isWrongAngle()) {
            // FIXME: implement a way to correct the hand's angle
        }
    }

}
