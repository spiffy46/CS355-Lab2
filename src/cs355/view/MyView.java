package cs355.view;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.MyModel;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class MyView implements ViewRefresher{

	private MyModel modelUpdate;
	List<Shape> shapeList = new ArrayList<Shape>();
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof MyModel){
			modelUpdate = (MyModel) o;
			shapeList = modelUpdate.getShapes();
		} else {}
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		Graphics2D toDrawOn = (Graphics2D) g2d;
		for(int i = 0; i < shapeList.size(); i++){
			AffineTransform objToWorld = new AffineTransform();
			toDrawOn.setTransform(objToWorld);
			Shape s = shapeList.get(i);
			if(s instanceof Line){
				Line l = (Line) s;
				toDrawOn.setColor(l.getColor());
				toDrawOn.drawLine((int)l.getCenter().getX(), (int)l.getCenter().getY(), (int)l.getEnd().getX(), (int)l.getEnd().getY());
			} else if(s instanceof Square){
				Square sq =(Square) s;
				Point2D.Double center = sq.getCenter();
				double size = sq.getSize();
				
				objToWorld.translate(center.getX(), center.getY());
				objToWorld.rotate(sq.getRotation());
				toDrawOn.setTransform(objToWorld);
				toDrawOn.setColor(sq.getColor());
				toDrawOn.fillRect((int)-size/2, (int)-size/2, (int)size, (int)size);
			} else if(s instanceof Rectangle){
				Rectangle r = (Rectangle) s;
				Point2D.Double center = r.getCenter();
				double width = r.getWidth();
				double height = r.getHeight();
				
				objToWorld.translate(center.getX(), center.getY());
				objToWorld.rotate(r.getRotation());
				toDrawOn.setTransform(objToWorld);
				toDrawOn.setColor(r.getColor());
				toDrawOn.fillRect((int)(-width/2), (int)(-height/2), (int)width, (int)height);
			} else if(s instanceof Circle){
				Circle c = (Circle) s;
				Double radius = c.getRadius();
				toDrawOn.setColor(c.getColor());
				toDrawOn.fillOval((int)(c.getCenter().getX() - radius), (int)(c.getCenter().getY() - radius), (int)(radius*2), (int)(radius*2));
			}else if(s instanceof Ellipse){
				Ellipse el = (Ellipse) s;
				Point2D.Double center = el.getCenter();
				int w = (int)el.getWidth();
				int h = (int)el.getHeight();
				
				objToWorld.translate(center.getX(), center.getY());
				objToWorld.rotate(el.getRotation());
				toDrawOn.setTransform(objToWorld);
				toDrawOn.setColor(el.getColor());
				toDrawOn.fillOval((int)-w/2, (int)-h/2, w, h);
			}else if(s instanceof Triangle){
				Triangle t = (Triangle) s;
				Point2D.Double center = t.getCenter();
				int[] x = {(int)(t.getA().x-center.getX()),(int)(t.getB().x-center.getX()),(int)(t.getC().x-center.getX())};
				int[] y = {(int)(t.getA().y-center.getY()),(int)(t.getB().y-center.getY()),(int)(t.getC().y-center.getY())};
				
				objToWorld.translate(center.getX(), center.getY());
				objToWorld.rotate(t.getRotation());
				toDrawOn.setTransform(objToWorld);
				toDrawOn.setColor(t.getColor());
				toDrawOn.fillPolygon(x, y, 3);
			}else{}
		}
	}

	public void setModel(MyModel model) {
		modelUpdate = model;
		modelUpdate.addObserver(this);
	}
	
}
