import java.util.*;
import java.util.stream.Collectors;

class Cell {

  private final Player owner;

  private final Pos pos;

  private final int scrapAmount;

  private final int units;

  private final int recycler;

  private final boolean canBuild;

  private final boolean canSpawn;

  private final boolean inRangeOfRecycler;

  Cell(
      Player owner,
      Pos pos,
      int scrapAmount,
      int units,
      int recycler,
      boolean canBuild,
      boolean canSpawn,
      boolean inRangeOfRecycler) {
    this.owner = owner;
    this.pos = pos;
    this.scrapAmount = scrapAmount;
    this.units = units;
    this.recycler = recycler;
    this.canBuild = canBuild;
    this.canSpawn = canSpawn;
    this.inRangeOfRecycler = inRangeOfRecycler;
  }

  public Player getOwner() {
    return owner;
  }

  public Pos getPos() {
    return pos;
  }

  public int getScrapAmount() {
    return scrapAmount;
  }

  public int getUnits() {
    return units;
  }

  public int getRecycler() {
    return recycler;
  }

  public boolean isCanBuild() {
    return canBuild;
  }

  public boolean isCanSpawn() {
    return canSpawn;
  }

  public boolean isInRangeOfRecycler() {
    return inRangeOfRecycler;
  }

  public Collection<Cell> getNeighbors(Map<Pos, Cell> cells) {
    List<Pos> neighbors = new ArrayList<>();
    neighbors.add(Pos.of(pos.getX() + 1, pos.getY()));
    neighbors.add(Pos.of(pos.getX() - 1, pos.getY()));
    neighbors.add(Pos.of(pos.getX(), pos.getY() + 1));
    neighbors.add(Pos.of(pos.getX(), pos.getY() - 1));
    return neighbors.stream()
        .map(npos -> cells.get(npos))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public boolean isGrass() {
    return scrapAmount == 0;
  }

  public boolean isNotGrass() {
    return scrapAmount > 0;
  }
}
