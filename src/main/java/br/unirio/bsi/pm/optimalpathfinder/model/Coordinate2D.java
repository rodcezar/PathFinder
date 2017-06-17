package br.unirio.bsi.pm.optimalpathfinder.model;

public class Coordinate2D {
    
	/**
	 * This class is just a wrapper of an x and y coordinate. 
	 */
	
	private int x;
    private int y;
    
    /**
     * Getters and setters
     */
    public int getX(){return x;}
    public int getY(){return y;}
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
    
    /**
     * Constructor with default values
     */
    public Coordinate2D(){
    }
    
    /**
     * Constructor with given coordinates
     */
    public Coordinate2D(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
	/**
	 * sum a coordinate to this one
	 */    
    public void sumCoordinate(Coordinate2D a)
    {
    	this.setX(this.getX() + a.getX()); 
    	this.setY(this.getY() + a.getY()); 
    }
}


