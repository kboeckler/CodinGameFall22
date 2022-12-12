import java.util.*;
import java.util.stream.Collectors;

class Player {

  private final int id;

  private final int matter;

  Player(int id, int matter) {
    this.id = id;
    this.matter = matter;
  }

  public int getId() {
    return id;
  }

  public int getMatter() {
    return matter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return id == player.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int width = in.nextInt();
    int height = in.nextInt();

    // game loop
    while (true) {

      int myMatter = in.nextInt();
      int oppMatter = in.nextInt();

      Player me = new Player(1, myMatter);
      Player opp = new Player(2, oppMatter);
      Map<Pos, Cell> map = new HashMap<>();

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int scrapAmount = in.nextInt();
          int owner = in.nextInt(); // 1 = me, 0 = foe, -1 = neutral
          int units = in.nextInt();
          int recycler = in.nextInt();
          int canBuild = in.nextInt();
          int canSpawn = in.nextInt();
          int inRangeOfRecycler = in.nextInt();
          Cell cell =
              new Cell(
                  owner == 1 ? me : owner == 0 ? opp : null,
                  Pos.of(x, y),
                  scrapAmount,
                  units,
                  recycler,
                  canBuild == 1,
                  canSpawn == 1,
                  inRangeOfRecycler == 1);
          map.put(cell.getPos(), cell);
        }
      }

      Game game = new Game();
      game.setPlayers(Arrays.asList(me, opp));
      game.setMap(map);

      List<Action> actions = game.findNextActions();
      if (actions.isEmpty()) {
        System.out.println("WAIT");
      } else {
        System.out.println(actions.stream().map(Action::output).collect(Collectors.joining(";")));
      }
    }
  }
}
