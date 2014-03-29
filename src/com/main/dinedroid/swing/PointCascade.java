package com.main.dinedroid.swing;

import java.awt.Point;
import java.io.Serializable;

public class PointCascade 
implements Cloneable, Serializable 
{ 
  private Cascade xCascade; 
  private Cascade yCascade; 

  /** Creates a new PointCascade with default logical parameters. 
  **/ 
  public PointCascade () 
  { 
    setXCascade(new Cascade(10,0,25,375)); 
    setYCascade(new Cascade(10,0,25,300)); 
  } 

  /** Creates a new PointCascade with the given Cascades. 
    @param xCascade the Cascade for the x dimension 
    @param yCascade the Cascade for the y dimension 
    **/ 
  public PointCascade (Cascade xCascade, Cascade yCascade) 
  { 
    setXCascade(xCascade); 
    setYCascade(yCascade); 
  } 

  /** Creates a new Cascade with the given logical parameters. 
    @param offset the offset from 0,0 
    @param position the position from the offset 
    @param step the distance the position is incremented 
    @param wrap the basis of the position modulus 
    **/ 
  public PointCascade (Point offset, Point position, Point step, Point wrap) 
  { 
    this(); 
    setOffset(offset); 
    setPosition(position); 
    setStep(step); 
    setWrap(wrap); 
  } 

  /** Sets the instance's x dimension Cascade 
    @param xCascade the Cascade for the x dimension 
    **/ 
  public void setXCascade (Cascade xCascade) 
  { 
    this.xCascade = xCascade; 
  } 

  /** Gets the instance's x dimension Cascade 
    @return the Cascade for the x dimension 
    **/ 
  public Cascade getXCascade () 
  { 
    return xCascade; 
  } 

  /** Sets the instance's y dimension Cascade 
    @param xCascade the Cascade for the y dimension 
    **/ 
  public void setYCascade (Cascade yCascade) 
  { 
    this.yCascade = yCascade; 
  } 

  /** Gets the instance's y dimension Cascade 
    @return the Cascade for the y dimension 
    **/ 
  public Cascade getYCascade () 
  { 
    return yCascade; 
  } 

  /** Sets the instance's logical offset 
    @param offset the offset from 0,0 
    **/ 
  public void setOffset (Point offset) 
  { 
    xCascade.setOffset(offset.x); 
    yCascade.setOffset(offset.y); 
  } 

  /** Gets the instance's logical offset 
    @return the offset from 0,0 
    **/ 
  public Point getOffset () 
  { 
    return new Point(xCascade.getOffset(),yCascade.getOffset()); 
  } 

  /** Sets the instance's logical position 
    @param position the position from the offset 
    **/ 
  public void setPosition (Point position) 
  { 
    xCascade.setPosition(position.x); 
    yCascade.setPosition(position.y); 
  } 

  /** Gets the instance's logical position 
    @return the position from the offset 
    **/ 
  public Point getPosition () 
  { 
    return new Point(xCascade.getPosition(),yCascade.getPosition()); 
  } 

  /** Sets the instance's logical step 
    @param step the distance the position is incremented 
    **/ 
  public void setStep (Point step) 
  { 
    xCascade.setStep(step.x); 
    yCascade.setStep(step.y); 
  } 

  /** Gets the instance's logical step 
    @return the distance the position is incremented 
    **/ 
  public Point getStep () 
  { 
    return new Point(xCascade.getStep(),yCascade.getStep()); 
  } 

  /** Sets the instance's logical wrap 
    @param wrap the basis of the position modulus 
    **/ 
  public void setWrap (Point wrap) 
  { 
    xCascade.setWrap(wrap.x); 
    yCascade.setWrap(wrap.y); 
  } 

  /** Gets the instance's logical wrap 
    @return the basis of the position modulus 
    **/ 
  public Point getWrap () 
  { 
    return new Point(xCascade.getWrap(),yCascade.getWrap()); 
  } 

  /** Computes the instance's current location. 
    @return the location 
    **/ 
  public Point location () 
  { 
    return new Point(xCascade.location(),yCascade.location()); 
  } 

  /** Increments the instance's location, keeping it within the parameters. 
  **/ 
  public void increment () 
  { 
    xCascade.increment(); 
    yCascade.increment(); 
  } 

  /** Compares the contents of the instance with the given PointCascade's 
    contents. 
    @return true if the contents of the instance and the given pointCascade 
    are the same 
  **/ 
  public boolean equals (PointCascade pointCascade) 
  { 
    return 
      ((pointCascade != null) && 
       (getOffset().equals(pointCascade.getOffset())) && 
       (getPosition().equals(pointCascade.getPosition())) && 
       (getStep().equals(pointCascade.getStep())) && 
       (getWrap().equals(pointCascade.getWrap()))); 
  } 

  /** Clones the instance. 
    @return the cloned instance 
    **/ 
  public Object clone () 
  { 
    PointCascade clone = null; 
    try 
      { 
 clone = (PointCascade)(super.clone()); 
      } 
    catch (CloneNotSupportedException exception) 
      { 
 throw new InternalError(exception.toString()); 
      } 
    clone.xCascade = (Cascade)(xCascade.clone()); 
    clone.yCascade = (Cascade)(yCascade.clone()); 
    return clone(); 
  } 
} 