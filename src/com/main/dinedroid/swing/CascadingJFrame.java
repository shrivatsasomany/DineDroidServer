package com.main.dinedroid.swing;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Hashtable;

import javax.swing.JFrame;

/** A CascadingJFrame implements an initially autopositioning JFrame using a 
named PointCascade to assign the instance's location. 
A Hashtable of PointCascades, keyed by name, is maintained in the class. 
It is accessed when the CascadingJFrame is constructed to get the initial 
location. 
Afterwards, on the first call to setBounds, the JFrame's position is 
adjusted to the cascaded location. 
The autopositioning may be turned off or adjusted on an instance prior to 
the setBounds call. 
**/ 

public class CascadingJFrame 
extends JFrame 
implements Serializable 
{ 
private static Hashtable pointCascadeHashtable = new Hashtable (); 
/** The name of the default PointCascade **/ 
public static final String defaultCascadeName = "_default_"; 

private Point location; 
private boolean autoposition; 

/** Creates a new CascadingJFrame with the given title using the default 
  PointCascade. 
  @param title the text to be placed on the title bar 
**/ 
public CascadingJFrame (String title) 
{ 
  this(title,defaultCascadeName,true); 
} 

/** Creates a new CascadingJFrame with the given title using the named 
  PointCascade. 
  @param title the text to be placed on the title bar 
  @param cascadeName the name of the PointCascade to retrieve the location 
  from 
  **/ 
public CascadingJFrame (String title, String cascadeName) 
{ 
  this(title,cascadeName,true); 
} 

/** Creates a new CascadingJFrame with the given title using the named 
  PointCascade. 
  @param title the text to be placed on the title bar 
  @param cascadeName the name of the PointCascade to retrieve the location 
  from 
  @param autoposition if false, then no autopositioning will be done for the 
  instance 
  **/ 
public CascadingJFrame 
(String title, String cascadeName, boolean autoposition) 
{ 
  super(title); 
  if (autoposition) 
    setLocation(cascadeName); 
  setAutoposition(autoposition); 
} 

/** Sets the default PointCascade to the given PointCascade. 
  @param pointCascade the new default PointCascade 
  **/ 

public static void setPointCascade (PointCascade pointCascade) 
{ 
  setPointCascade(defaultCascadeName,pointCascade); 
} 

/** Sets the named PointCascade to the given PointCascade. 
  @param cascadeName the name of the PointCascade 
  @param pointCascade the new PointCascade 
  **/ 
public static void setPointCascade 
(String cascadeName, PointCascade pointCascade) 
{ 
  pointCascadeHashtable.put(cascadeName,pointCascade); 
} 

/** Gets the default PointCascade. 
  @return the default PointCascade 
  **/ 
public static PointCascade getPointCascade () 
{ 
  return CascadingJFrame.getPointCascade(defaultCascadeName); 
} 

/** Gets the named PointCascade. 
  If a point cascade by that name does not exist, a new one is created. 
  @param cascadeName the name of the PointCascade 
  @return the named PointCascade 
  **/ 
public static PointCascade getPointCascade (String cascadeName) 
{ 
  if (cascadeName == null) 
    cascadeName = defaultCascadeName; 
  PointCascade pointCascade = 
    (PointCascade)(pointCascadeHashtable.get(cascadeName)); 
  if (pointCascade == null) 
    { 
pointCascade = new PointCascade(); 
CascadingJFrame.setPointCascade(cascadeName,pointCascade); 
    } 
  return pointCascade; 
} 

/** Sets whether the instance will be autopositioned. 
  @param autoposition true if autopositioning is desired 
  **/ 
public void setAutoposition (boolean autoposition) 
{ 
  this.autoposition = autoposition; 
} 

/** Gets whether the instance will be autopositioned. 
  @return true if autopositioning will occur 
  **/ 
public boolean getAutoposition () 
{ 
  return autoposition; 
} 

/** Sets the instance's location by retrieving it from the named 
  PointCascade. 
  @param cascadeName the name of the PointCascade 
  **/ 
public void setLocation (String cascadeName) 
{ 
  PointCascade pointCascade = getPointCascade(cascadeName); 
  setLocation(pointCascade.location()); 
  pointCascade.increment(); 
} 

/** Sets the instance's location to the specified Point. 
  @param location the new location 
  **/ 
public void setLocation (Point location) 
{ 
  this.location = location; 
} 

/** Gets the instance's location. 
  @return the location 
  **/ 
public Point getLocation () 
{ 
  return location; 
} 

/** Sets the instance's bounds, autopositioning if intended. 
  @param x the new x coordinate of the upper left position 
  @param y the new y coordinate of the upper left position 
  @param width the new width of the instance 
  @param height the new height of the instance 
  **/ 
public void setBounds (int x, int y, int width, int height) 
{ 
  if (getAutoposition() == true) 
    { 
Point frameLocation = getLocation(); 
x = frameLocation.x; 
y = frameLocation.y; 
setAutoposition(false); 
    } 
  super.setBounds(x,y,width,height); 
} 

/** Sets the instance's bounds, autopositioning if intended. 
  @param r the new bounds of the instance 
  **/ 
public void setBounds (Rectangle r) 
{ 
  setBounds(r.x,r.y,r.width,r.height); 
} 
} 