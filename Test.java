public class Test {
    public static void main(String[] args) {
        Player warrior = new Player(new WarriorAction(), new WarriorMissChance());
        warrior.act();
    }
}