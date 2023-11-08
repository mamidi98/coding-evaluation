package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Optional;

public abstract class Organization {

    private Position root;

    private int identifier;
    
    public Organization() {
        root = createOrganization();
        this.identifier=1;
    }
    
    protected abstract Position createOrganization();
    
    /**
     * hire the given person as an employee in the position that has that title
     * 
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
        boolean isPresent = checkPosition(root, title);
        if(!isPresent){
            return Optional.empty();
        }
        Position position = new Position(title);
        position.setEmployee(Optional.of(new Employee(createID(), person)));
        position.addDirectReport(getDirectReport(title,root));
        return Optional.of(position);
    }

    private Position getDirectReport(String title, Position root) {
        if(root.getTitle().equals(title)){
            return root;
        }
        Collection<Position> subordinates = root.getDirectReports();
        for(Position position: subordinates){
            Position currPosition = getDirectReport(title, position);
            if(currPosition!=null) return currPosition;
        }
        return null;
    }

    private int createID() {
        return this.identifier++;
    }

    private boolean checkPosition(Position root, String title) {
        if(root.getTitle().equals(title)) return true;
        Collection<Position> subordinates = root.getDirectReports();
        for(Position position: subordinates){
            if(checkPosition(position,title)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return printOrganization(root, "");
    }
    
    private String printOrganization(Position pos, String prefix) {
        StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
        for(Position p : pos.getDirectReports()) {
            sb.append(printOrganization(p, prefix + "  "));
        }
        return sb.toString();
    }
}
