import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Game {

  private List<Player> players;

  private Map<Pos, Cell> map;

  void setPlayers(List<Player> players) {
    this.players = players;
  }

  void setMap(Map<Pos, Cell> map) {
    this.map = map;
  }

  List<Action> findNextActions() {
    return Stream.concat(findMatterActions(), findMoveActions().stream())
        .collect(Collectors.toList());
  }

  private Stream<Action> findMatterActions() {
    List<ActionBuildRecycler> buildActions = findBuildActions().collect(Collectors.toList());
    int matter = players.get(0).getMatter() - buildActions.size() * 10;
    Stream<ActionSpawn> actionSpawnStream =
        map.values().stream()
            .filter(Cell::isCanSpawn)
            .sorted(
                (one, another) ->
                    (int)
                        (another.getNeighbors(map).stream()
                                .filter(Cell::isNotGrass)
                                .filter(n -> !players.get(0).equals(n.getOwner()))
                                .count()
                            - one.getNeighbors(map).stream()
                                .filter(Cell::isNotGrass)
                                .filter(n -> !players.get(0).equals(n.getOwner()))
                                .count()))
            .limit(matter / 10)
            .map(cell -> new ActionSpawn(1, cell.getPos()));
    return Stream.concat(buildActions.stream(), actionSpawnStream);
  }

  private Stream<ActionBuildRecycler> findBuildActions() {
    int matter = players.get(0).getMatter();
    Set<Pos> cellsBuilding = new HashSet<>();
    for (Cell cell : map.values()) {
      if (cell.isCanBuild() && matter >= 10 /* && !cellsBuilding.contains(cell.getPos())*/) {
        if (Stream.concat(Stream.of(cell), cell.getNeighbors(map).stream())
                .filter(c -> c.getScrapAmount() >= 4)
                .filter(c -> !c.isInRangeOfRecycler())
                .filter(
                    c ->
                        c.getNeighbors(map).stream()
                            .map(Cell::getPos)
                            .noneMatch(cellsBuilding::contains))
                .count()
            == 5) {
          cellsBuilding.add(cell.getPos());
          matter -= 10;
        }
      }
    }
    return cellsBuilding.stream().map(ActionBuildRecycler::new);
  }

  private List<ActionMove> findMoveActions() {
    List<ActionMove> moves = new ArrayList<>();
    Map<Pos, Integer> unitsPerPos = new HashMap<>();
    for (Cell cell : map.values()) {
      if (players.get(0).equals(cell.getOwner()) && cell.getUnits() > 0) {
        unitsPerPos.put(cell.getPos(), cell.getUnits());
      }
    }
    Set<Pos> cellsMoving = new HashSet<>();
    for (Map.Entry<Pos, Integer> units : unitsPerPos.entrySet()) {
      for (int i = 0; i < units.getValue(); i++) {
        Pos from = units.getKey();
        Pos closest = null;
        double minDist2 = Double.MAX_VALUE;
        for (Cell cell : map.values()) {
          if (cellsMoving.contains(cell.getPos())) {
            continue;
          }
          if (cell.isGrass()) {
            continue;
          }
          if (players.get(0).equals(cell.getOwner())) {
            continue;
          }
          double d2 = cell.getPos().dist2To(from);
          if (d2 < minDist2) {
            minDist2 = d2;
            closest = cell.getPos();
          }
        }
        if (closest != null) {
          cellsMoving.add(closest);
          moves.add(new ActionMove(1, from, closest));
        }
      }
    }
    return moves;
  }
}
