package cs355.view;

import java.awt.Color;
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
				Point2D.Double center = l.getCenter();

				objToWorld.translate(center.getX(), center.getY());
				objToWorld.rotate(l.getRotation());
				toDrawOn.setTransform(objToWorld);
				toDrawOn.setColor(l.getColor());
				toDrawOn.drawLine((int)0, (int)0, (int)(l.getEnd().getX()-center.getX()), (int)(l.getEnd().getY()-center.getY()));
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
				
				objToWorld.translate(c.getCenter().getX(), c.getCenter().getY());
				objToWorld.rotate(c.getRotation());
				toDrawOn.setTransform(objToWorld);
				toDrawOn.setColor(c.getColor());
				toDrawOn.fillOval((int)-radius, (int)-radius, (int)(radius*2), (int)(radius*2));
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
		Shape selectedShape = modelUpdate.getSelectedShape();
		if (selectedShape != null) {
			drawHandles(selectedShape, g2d);
		}
	}

	public void drawHandles(Shape s, Graphics2D g2d) {
		AffineTransform objToWorld = new AffineTransform();
		Graphics2D toDrawOn = (Graphics2D) g2d;

		if(s instanceof Square){
			Square sq =(Square) s;
			Point2D.Double center = sq.getCenter();
			double size = sq.getSize();
			
			objToWorld.translate(center.getX(), center.getY());
			objToWorld.rotate(sq.getRotation());
			toDrawOn.setTransform(objToWorld);
			toDrawOn.setColor(Color.RED);
			toDrawOn.drawRect((int)-size/2, (int)-size/2, (int)size, (int)size);
			
			double c = size/2 + 20;
			toDrawOn.drawOval((int)-5, (int)-(c+5), 10, 10);
		} else if(s instanceof Rectangle){
			Rectangle r = (Rectangle) s;
			Point2D.Double center = r.getCenter();
			double width = r.getWidth();
			double height = r.getHeight();
			
			objToWorld.translate(center.getX(), center.getY());
			objToWorld.rotate(r.getRotation());
			toDrawOn.setTransform(objToWorld);
			toDrawOn.setColor(Color.RED);
			toDrawOn.drawRect((int)(-width/2), (int)(-height/2), (int)width, (int)height);
			
			double c = height/2 + 20;
			double x = c * Math.sin(r.getRotation());
			double y = c * Math.cos(r.getRotation());
			toDrawOn.drawOval((int)-(x+5), (int)-(y+5), 10, 10);
		} else if(s instanceof Circle){
			Circle c = (Circle) s;
			Double radius = c.getRadius();
			
			objToWorld.translate(c.getCenter().getX(), c.getCenter().getY());
			objToWorld.rotate(c.getRotation());
			toDrawOn.setTransform(objToWorld);
			toDrawOn.setColor(Color.RED);
			toDrawOn.drawRect((int)-radius, (int)-radius, (int)(radius*2), (int)(radius*2));
		}else if(s instanceof Ellipse){
			Ellipse el = (Ellipse) s;
			Point2D.Double center = el.getCenter();
			int w = (int)el.getWidth();
			int h = (int)el.getHeight();
			
			objToWorld.translate(center.getX(), center.getY());
			objToWorld.rotate(el.getRotation());
			toDrawOn.setTransform(objToWorld);
			toDrawOn.setColor(Color.RED);
			toDrawOn.drawRect((int)-w/2, (int)-h/2, w, h);
			
			double c = h/2 + 20;
			double x = c * Math.sin(el.getRotation());
			double y = c * Math.cos(el.getRotation());
			toDrawOn.drawOval((int)-(x+5), (int)-(y+5), 10, 10);
		}else if(s instanceof Triangle){
			Triangle t = (Triangle) s;
			Point2D.Double center = t.getCenter();
			int[] xc = {(int)(t.getA().x-center.getX()),(int)(t.getB().x-center.getX()),(int)(t.getC().x-center.getX())};
			int[] yc = {(int)(t.getA().y-center.getY()),(int)(t.getB().y-center.getY()),(int)(t.getC().y-center.getY())};
			
			objToWorld.translate(center.getX(), center.getY());
			objToWorld.rotate(t.getRotation());
			toDrawOn.setTransform(objToWorld);
			toDrawOn.setColor(Color.RED);
			toDrawOn.drawPolygon(xc, yc, 3);
			
			double lca = Math.sqrt(Math.pow((t.getCenter().getX() - t.getA().getX()), 2) + Math.pow((t.getCenter().getY() - t.getA().getY()), 2));
			double lcb = Math.sqrt(Math.pow((t.getCenter().getX() - t.getB().getX()), 2) + Math.pow((t.getCenter().getY() - t.getB().getY()), 2));
			double lcc = Math.sqrt(Math.pow((t.getCenter().getX() - t.getC().getX()), 2) + Math.pow((t.getCenter().getY() - t.getC().getY()), 2));

			double c = Math.max(lca, Math.max(lcb,lcc));
			double x = c * Math.sin(t.getRotation());
			double y = c * Math.cos(t.getRotation());
			toDrawOn.drawOval((int)-(x+5), (int)-(y+5), 10, 10);
		}else{}
	}
	
	public void setModel(MyModel model) {
		modelUpdate = model;
		modelUpdate.addObserver(this);
	}
	
}
