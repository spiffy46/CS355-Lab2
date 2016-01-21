package cs355.model.drawing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs355.GUIFunctions;

public class MyModel extends CS355Drawing{

	List<Shape> shapeList = new ArrayList<Shape>();
	
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
	}

	@Override
	public void moveToFront(int index) {
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		ArrayList<Shape> newList = new ArrayList<Shape>();
		newList.add(s);
		newList.addAll(shapeList);
	}

	@Override
	public void movetoBack(int index) {
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		shapeList.add(s);
	}

	@Override
	public void moveForward(int index) {
		if(index != 0){
			Shape s = shapeList.get(index);
			Shape s1 = shapeList.get(index-1);
			shapeList.set(index, s1);
			shapeList.set(index-1, s);
		}
	}

	@Override
	public void moveBackward(int index) {
		if(index != shapeList.size()-1){
			Shape s = shapeList.get(index);
			Shape s1 = shapeList.get(index+1);
			shapeList.set(index, s1);
			shapeList.set(index+1, s);
		}
	}

	@Override
	public List<Shape> getShapes() {
		return shapeList;
	}

	@Override
	public List<Shape> getShapesReversed() {
		List<Shape> r = shapeList;
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
	
	public Shape geometryTest(Point2D worldCoord, int tolerance) {
		AffineTransform worldToObj = new AffineTransform();
		Point2D.Double objCoord = new Point2D.Double();
		List<Shape> reversed = this.getShapesReversed();
		
		for(int i = 0; i < reversed.size(); i++){
			Shape s = reversed.get(i);
			worldToObj.rotate(-s.getRotation());
			worldToObj.translate(-s.getCenter().getX(), -s.getCenter().getY());
			worldToObj.transform(worldCoord, objCoord);
			GUIFunctions.printf(objCoord.toString());
			if(s instanceof Line){
				//TODO Finish Line selection
				/*Line l = (Line)s;
				if (Math.abs(objCoord.getX())<sq.getSize()/2+tolerance && Math.abs(objCoord.getY())<sq.getSize()/2+tolerance){
					GUIFunctions.printf("Selected Line");
					return sq;
				}*/
			} else if(s instanceof Square){
				Square sq = (Square)s;
				if (Math.abs(objCoord.getX())<sq.getSize()/2+tolerance && Math.abs(objCoord.getY())<sq.getSize()/2+tolerance){
					GUIFunctions.printf("Selected Square");
					return sq;
				}
			} else if(s instanceof Rectangle){
				Rectangle r = (Rectangle)s;
				if (Math.abs(objCoord.getX())<r.getWidth()/2+tolerance && Math.abs(objCoord.getY())<r.getHeight()/2+tolerance){
					GUIFunctions.printf("Selected Rectangle");
					return r;
				}
			} else if(s instanceof Circle){
				Circle c = (Circle)s;
				if (objCoord.getX()*objCoord.getX() + objCoord.getY()*objCoord.getY() < (c.getRadius()*c.getRadius())+tolerance){
					GUIFunctions.printf("Selected Circle");
					return c;
				}
			}else if(s instanceof Ellipse){
				Ellipse el = (Ellipse)s;
				double a = el.getWidth()/2+tolerance;
				double b = el.getHeight()/2+tolerance;
				if ((objCoord.getX()*objCoord.getX())/(a*a) + (objCoord.getY()*objCoord.getY())/(b*b) <= 1){
					GUIFunctions.printf("Selected Ellipse");
					return el;
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
					return t;
				}
			}else{}
		}
		return null;
	}
	
	public double calcArea(Point2D A, Point2D B, Point2D C) {
		double area = Math.abs(A.getX() * (B.getY()-C.getY()) + B.getX() * (C.getY() - A.getY()) + C.getX() * (A.getY() - B.getY()));
		return area/2;
	}

}
