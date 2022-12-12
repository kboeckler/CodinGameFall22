class ActionMove implements Action {

  private final int amount;

  private final Pos from;

  private final Pos to;

  public ActionMove(int amount, Pos from, Pos to) {
    this.amount = amount;
    this.from = from;
    this.to = to;
  }

  @Override
  public String output() {
    return String.format(
        "MOVE %d %d %d %d %d", amount, from.getX(), from.getY(), to.getX(), to.getY());
  }
}
