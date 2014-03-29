package com.main.dinedroid.swing;
import java.io.Serializable; 

/** A Cascade implements a stepable position in a one-dimensional offset 
  wrapping-space. 
  The instance computes a new int location and increments it on demand. 
  The four int parameters which control the instance are 
  <UL> 
  <LI>the <I>offset</I> from the origin, 
  <LI>the current <I>position</I> relative to the offset, 
  <LI>the <I>step</I> which is added to the position during an increment, and 
  <LI>the <I>wrap</I> which bounds the position relative to the offset. 
  </UL> 
**/ 

public class Cascade 
implements Cloneable, Serializable 
{ 
  private int offset; 
  private int position; 
  private int step; 
  private int wrap; 

  /** Creates a new Cascade with default parameters. 
  **/ 
  public Cascade () 
  { 
    this(10,0,25,300); 
  } 

  /** Creates a new Cascade with the given parameters. 
    @param offset the offset from 0 
    @param position the position from the offset 
    @param step the distance the position is incremented 
    @param wrap the basis of the position modulus 
    **/ 
  public Cascade (int offset, int position, int step, int wrap) 
  { 
    setOffset(offset); 
    setPosition(position); 
    setStep(step); 
    setWrap(wrap); 
  } 

  /** Sets the instance's offset. 
    @param offset the offset from 0 
    **/ 
  public void setOffset (int offset) 
  { 
    this.offset = offset; 
  } 

  /** Gets the instance's offset. 
    @return the offset from 0 
    **/ 
  public int getOffset () 
  { 
    return offset; 
  } 

  /** Sets the instance's position. 
    @param position the position from the offset 
    **/ 
  public void setPosition (int position) 
  { 
    this.position = position; 
  } 

  /** Gets the instance's position. 
    @return the position from the offset 
    **/ 
  public int getPosition () 
  { 
    return position; 
  } 

  /** Sets the instance's step. 
    @param step the distance the position is incremented 
    **/ 
  public void setStep (int step) 
  { 
    this.step = step; 
  } 

  /** Gets the instance's step. 
    @return the distance the position is incremented 
    **/ 
  public int getStep () 
  { 
    return step; 
  } 

  /** Sets the instance's wrap. 
    @param wrap the basis of the position modulus 
    **/ 
  public void setWrap (int wrap) 
  { 
    this.wrap = wrap; 
  } 

  /** Gets the instance's wrap. 
    @return the basis of the position modulus 
    **/ 
  public int getWrap () 
  { 
    return wrap; 
  } 

  /** Computes the instance's current location. 
    @return the location 
    **/ 
  public int location () 
  { 
    return offset+position; 
  } 

  /** Increments the instance's location, keeping it within the parameters. 
  **/ 
  public void increment () 
  { 
    position += step; 
    while (position < 0) 
      position += wrap; 
    while (position > wrap) 
      position -= wrap; 
    if (position < 0) 
      position = 0; 
  } 

  /** Compares the contents of the instance with the given Cascade's contents. 
    @return true if the contents of the instance and the given Cascade are 
    the same 
  **/ 
  public boolean equals (Cascade cascade) 
  { 
    return 
      ((cascade != null)&& 
       (getOffset() == cascade.getOffset())&& 
       (getPosition() == cascade.getPosition())&& 
       (getStep() == cascade.getStep())&& 
       (getWrap() == cascade.getWrap())); 
  } 

  /** Clones the instance. 
    @return the cloned instance 
    **/ 
  public Object clone () 
  { 
    Cascade clone = null; 
    try 
      { 
 clone = (Cascade)(super.clone()); 
      } 
    catch (CloneNotSupportedException exception) 
      { 
 throw new InternalError(exception.toString()); 
      } 
    return clone(); 
  } 
} 