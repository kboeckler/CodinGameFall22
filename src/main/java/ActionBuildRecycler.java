class ActionBuildRecycler implements Action {

  private final Pos pos;

  public ActionBuildRecycler(Pos pos) {
    this.pos = pos;
  }

  @Override
  public String output() {
    return String.format("BUILD %d %d", pos.getX(), pos.getY());
  }
}
