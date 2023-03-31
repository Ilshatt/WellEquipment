package model;

public class Equipment extends EntityBase {

    public Equipment(int id, String name, Well well) {
        super(id, name);
        this.well = well;
    }
    private Well well;

    public Well getWell() {
        return well;
    }
}
