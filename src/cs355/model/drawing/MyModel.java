package cs355.model.drawing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs355.GUIFunctions;

public class MyModel extends CS355Drawing{

	List<Shape> shapeList = new ArrayList<Shape>();
	Shape selectedShape;
	
	@Override
	public Shape getShape(int index) {
		return shapeList.get(index);
	}

	@Override
	public int addShape(Shape s) {
		shapeList.add(s);
		setChanged();
		notifyObservers();
		return (shapeList.size()-1);
	}

	@Override
	public void deleteShape(int index) {
		shapeList.remove(index);
		setChanged();
		notifyObservers();
	}

	@Override
	public void moveToFront(int index) {
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		shapeList.add(s);
		setChanged();
		notifyObservers();
	}

	@Override
	public void movetoBack(int index) {
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		ArrayList<Shape> newList = new ArrayList<Shape>();
		newList.add(s);
		newList.addAll(shapeList);
		shapeList = new ArrayList<Shape>(newList);
		setChanged();
		notifyObservers();
	}

	@Override
	public void moveForward(int index) {
		Shape s = shapeList.get(index);
		Shape s1 = shapeList.get(index+1);
		shapeList.set(index, s1);
		shapeList.set(index+1, s);
		setChanged();
		notifyObservers();
	}

	@Override
	public void moveBackward(int index) {
		Shape s = shapeList.get(index);
		Shape s1 = shapeList.get(index-1);
		shapeList.set(index, s1);
		shapeList.set(index-1, s);
		setChanged();
		notifyObservers();
	}

	@Override
	public List<Shape> getShapes() {
		return shapeList;
	}

	@Override
	public List<Shape> getShapesReversed() {
		List<Shape> r = new ArrayList<Shape>(shapeList);
		Collections.reverse(r);
		return r;
	}

	@Override
	public void setShapes(List<Shape> shapes) {
		shapeList = shapes;
		setChanged();
		notifyObservers();
	}
	
	public int getSize() {
		return shapeList.size();
	}
	
	public int geometryTest(Point2D worldCoord, int tolerance) {
		Point2D.Double objCoord = new Point2D.Double();
		List<Shape> reversed = getShapesReversed();
		
		for(int i = 0; i < reversed.size(); i++){
			AffineTransform worldToObj = new AffineTransform();
			Shape s = reversed.get(i);
			worldToObj.rotate(-s.getRotation());
			worldToObj.translate(-s.getCenter().getX(), -s.getCenter().getY());
			worldToObj.transform(worldCoord, objCoord);
			GUIFunctions.printf(objCoord.toString());
			if(s instanceof Line){
				Line l = (Line)s;
				Point2D.Double d = new Point2D.Double();
				double x1 = l.getEnd().getX() - l.getCenter().getX();
				double y1 = l.getEnd().getY() - l.getCenter().getY();
				double x0 = 0;
				double y0 = 0;
				double lineLength = Math.sqrt((x1 - x0)*(x1 - x0) + (y1 - y0)*(y1 - y0));
				d.setLocation((x1 - x0)/lineLength, (y1 - y0)/lineLength);
				double t = (objCoord.getX()-x0)*d.getX() + (objCoord.getY()-y0)*d.getY();
				Point2D.Double q = new Point2D.Double();
				q.setLocation(x0 + t * d.getX(), y0 + t * d.getY());
				double qdist = Math.sqrt((objCoord.getX() - q.getX())*(objCoord.getX() - q.getX()) + (objCoord.getY() - q.getY())*(objCoord.getY() - q.getY()));
				if (qdist <= 4 && t >= -4 && t <= lineLength + 4){
					GUIFunctions.printf("Selected Line");
					selectedShape = l;
					setChanged();
					notifyObservers();
					return shapeList.size()-i-1;
				}
			} else if(s instanceof Square){
				Square sq = (Square)s;
				if (Math.abs(objCoord.getX())<sq.getSize()/2 && Math.abs(objCoord.getY())<sq.getSize()/2){
					GUIFunctions.printf("Selected Square");
					selectedShape = sq;
					setChanged();
					notifyObservers();
					return shapeList.size()-i-1;
				}
			} else if(s instanceof Rectangle){
				Rectangle r = (Rectangle)s;
				if (Math.abs(objCoord.getX())<r.getWidth()/2 && Math.abs(objCoord.getY())<r.getHeight()/2){
					GUIFunctions.printf("Selected Rectangle");
					selectedShape = r;
					setChanged();
					notifyObservers();
					return shapeList.size()-i-1;
				}
			} else if(s instanceof Circle){
				Circle c = (Circle)s;
				if (objCoord.getX()*objCoord.getX() + objCoord.getY()*objCoord.getY() < (c.getRadius()*c.getRadius())){
					GUIFunctions.printf("Selected Circle");
					selectedShape = c;
					setChanged();
					notifyObservers();
					return shapeList.size()-i-1;
				}
			}else if(s instanceof Ellipse){
				Ellipse el = (Ellipse)s;
				double a = el.getWidth()/2;
				double b = el.getHeight()/2;
				if ((objCoord.getX()*objCoord.getX())/(a*a) + (objCoord.getY()*objCoord.getY())/(b*b) <= 1){
					GUIFunctions.printf("Selected Ellipse");
					selectedShape = el;
					setChanged();
					notifyObservers();
					return shapeList.size()-i-1;
				}
			}else if(s instanceof Triangle){
				Triangle t = (Triangle)s;
				Point2D.Double a = new Point2D.Double(t.getA().getX() - t.getCenter().getX(), t.getA().getY() - t.getCenter().getY());
				Point2D.Double b = new Point2D.Double(t.getB().getX() - t.getCenter().getX(), t.getB().getY() - t.getCenter().getY());
				Point2D.Double c = new Point2D.Double(t.getC().getX() - t.getCenter().getX(), t.getC().getY() - t.getCenter().getY());

				double triArea = calcArea(a,b,c);
				double a1 = calcArea(objCoord,b,c);
				double a2 = calcArea(objCoord,a,c);
				double a3 = calcArea(objCoord,a,b);
				
				if(a1 + a2 + a3 <= triArea) {
					GUIFunctions.printf("Selected Triangle");
					selectedShape = t;
					setChanged();
					notifyObservers();
					return shapeList.size()-i-1;
				}
			}else{
				selectedShape = null;
				setChanged();
				notifyObservers();
			}
		}
		return -1;
	}
	
	public double calcArea(Point2D A, Point2D B, Point2D C) {
		double area = Math.abs(A.getX() * (B.getY()-C.getY()) + B.getX() * (C.getY() - A.getY()) + C.getX() * (A.getY() - B.getY()));
		return area/2;
	}

	public void setShape(int index, Shape s) {
		shapeList.set(index, s);
		setChanged();
		notifyObservers();
	}
	
	public Shape getSelectedShape() {
		return selectedShape;
	}
	
	public void setSelectedShape(int index){
		if(index > -1){
			selectedShape = shapeList.get(index);
		} else {
			selectedShape = null;
		}
		setChanged();
		notifyObservers();
	}
}
