package model;

import lombok.ToString;

public class OrganizationWithId extends Organization {
    @ToString.Exclude private static int idCounter = 1;
    private final Long id;

    public OrganizationWithId(final String properties) {
        super(properties);
        this.id = (long) idCounter++;
    }

    @Override
    public String toString() {
        return super.toString().replaceFirst("\\(", "\\(id=" + this.id + ", ");
    }

    @Override
    public String toJson() {
        return super.toJson();
    }

    public Long getId(){
        return id;
    }
}