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
    return Stream.concat(findBuildActions(), findMoveActions().stream())
        .collect(Collectors.toList());
  }

  private Stream<ActionBuildRecycler> findBuildActions() {
    int matter = players.get(0).getMatter();
    Set<Pos> cellsBuilding = new HashSet<>();
    for (Cell cell : map.values()) {
      if (cell.isCanBuild() && matter >= 10 && !cellsBuilding.contains(cell.getPos())) {
        cellsBuilding.add(cell.getPos());
        matter -= 10;
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
    List<Map.Entry<Pos, Integer>> availableUnits = new LinkedList<>();
    unitsPerPos.entrySet().forEach(availableUnits::add);
    Set<Pos> cellsMoving = new HashSet<>();
    // First loop looks for owner == null
    for (Cell cell : map.values()) {
      if (availableUnits.isEmpty()) {
        break;
      }
      if (cell.getOwner() == null && !cellsMoving.contains(cell.getPos())) {
        Map.Entry<Pos, Integer> nextMover = availableUnits.get(0);
        availableUnits.remove(0);
        cellsMoving.add(cell.getPos());
        moves.add(new ActionMove(nextMover.getValue(), nextMover.getKey(), cell.getPos()));
      }
    }
    // Second loop looks for owner == opp
    for (Cell cell : map.values()) {
      if (availableUnits.isEmpty()) {
        break;
      }
      if (players.get(1).equals(cell.getOwner()) && !cellsMoving.contains(cell.getPos())) {
        Map.Entry<Pos, Integer> nextMover = availableUnits.get(0);
        availableUnits.remove(0);
        cellsMoving.add(cell.getPos());
        moves.add(new ActionMove(nextMover.getValue(), nextMover.getKey(), cell.getPos()));
      }
    }
    return moves;
  }
}
