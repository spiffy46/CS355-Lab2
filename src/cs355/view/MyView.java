package cs355.view;

import java.awt.Graphics2D;
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
			Shape s = shapeList.get(i);
			if(s instanceof Line){
				Line l = (Line) s;
				toDrawOn.setColor(l.getColor());
				toDrawOn.drawLine((int)l.getStart().getX(), (int)l.getStart().getY(), (int)l.getEnd().getX(), (int)l.getEnd().getY());
			} else if(s instanceof Square){
				Square sq =(Square) s;
				toDrawOn.setColor(sq.getColor());
				toDrawOn.fillRect((int)sq.getUpperLeft().getX(), (int)sq.getUpperLeft().getY(), (int)sq.getSize(), (int)sq.getSize());
			} else if(s instanceof Rectangle){
				Rectangle r = (Rectangle) s;
				toDrawOn.setColor(r.getColor());
				toDrawOn.fillRect((int)r.getUpperLeft().getX(), (int)r.getUpperLeft().getY(), (int)r.getWidth(), (int)r.getHeight());
			} else if(s instanceof Circle){
				Circle c = (Circle) s;
				toDrawOn.setColor(c.getColor());
				Double radius = c.getRadius();
				toDrawOn.fillOval((int)(c.getCenter().getX() - radius), (int)(c.getCenter().getY() - radius), (int)(radius*2), (int)(radius*2));
			}else if(s instanceof Ellipse){
				Ellipse el = (Ellipse) s;
				toDrawOn.setColor(el.getColor());
				int w = (int)el.getWidth();
				int h = (int)el.getHeight();
				toDrawOn.fillOval((int)el.getCenter().getX() - w/2, (int)el.getCenter().getY() - h/2, w, h);
			}else if(s instanceof Triangle){
				Triangle t = (Triangle) s;
				toDrawOn.setColor(t.getColor());
				int[] x = {(int)t.getA().x,(int)t.getB().x,(int)t.getC().x};
				int[] y = {(int)t.getA().y,(int)t.getB().y,(int)t.getC().y};
				toDrawOn.fillPolygon(x, y, 3);
			}else{}
		}
	}

	public void setModel(MyModel model) {
		modelUpdate = model;
		modelUpdate.addObserver(this);
	}
	
}
