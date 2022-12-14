class ActionSpawn implements Action {

  private final int amount;

  private final Pos target;

  public ActionSpawn(int amount, Pos target) {
    this.amount = amount;
    this.target = target;
  }

  @Override
  public String output() {
    return String.format("SPAWN %d %d %d", amount, target.getX(), target.getY());
  }
}
