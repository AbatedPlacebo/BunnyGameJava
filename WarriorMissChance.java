public class WarriorMissChance implements MissStrategy {
    @Override
    public boolean isMissed() {
        return Math.random() < 0.15; // 15% miss
    }
}