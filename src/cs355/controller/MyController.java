package cs355.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import cs355.GUIFunctions;
import cs355.model.drawing.*;

public class MyController implements CS355Controller{
	
	MyModel model;
	Shape currentShape;
	List<Shape> shapeList;
	public Color col = Color.white;
	public String shape = "";
	public Point2D.Double p1;
	public Point2D.Double p2;
	public int triangleCount = 0;
	public Point2D.Double t1;
	public Point2D.Double t2;
	public Point2D.Double t3;

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		p1 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
		if (shape == "line"){
			currentShape = new Line(col,p1,p1);
		}else if(shape == "square"){
			currentShape = new Square(col,p1,0);
		}else if(shape == "rectangle"){
			currentShape = new Rectangle(col,p1,0,0);
		}else if(shape == "circle"){
			currentShape = new Circle(col,p1,0);
		}else if(shape == "ellipse"){
			currentShape = new Ellipse(col,p1,0,0);
		}else if (shape == "triangle"){
			if (triangleCount == 0){
				t1 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
				triangleCount++;
			}else if(triangleCount ==1){
				t2 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
				triangleCount++;
			}else{
				t3 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
				triangleCount = 0;
				Shape t = new Triangle(col,t1,t2,t3);
				model.addShape(t);
			}	
			return;
		}else{
			return;
		}
		model.addShape(currentShape);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		p2 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
		
		if (shape == "line"){
			currentShape = model.getShape(model.getSize()-1);
			Line l = (Line)currentShape;
			l.setEnd(p2);
			model.deleteShape(model.getSize()-1);
			model.addShape(l);
		}else if(shape == "square"){
			double size = Math.min((Math.abs(p1.getX()-p2.getX())),(Math.abs(p1.getY()-p2.getY())));
			Point2D.Double upLeft = new Point2D.Double();
			
			if(p1.getX() <= p2.getX() && p1.getY() <= p2.getY()){
				upLeft.setLocation(p1.getX(), p1.getY());
			}else if(p1.getX() <= p2.getX() && p1.getY() > p2.getY()){
				upLeft.setLocation(p1.getX(), p1.getY()-size);
			}else if(p1.getX() > p2.getX() && p1.getY() <= p2.getY()){
				upLeft.setLocation(p1.getX()-size, p1.getY());
			}else{
				upLeft.setLocation(p1.getX()-size, p1.getY()-size);
			}
			currentShape = model.getShape(model.getSize()-1);
			Square s = (Square)currentShape;
			s.setUpperLeft(upLeft);
			s.setSize(size);
			model.deleteShape(model.getSize()-1);
			model.addShape(s);
		}else if(shape == "rectangle"){
			Point2D.Double upLeft = new Point2D.Double();
			upLeft.setLocation(Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(),p2.getY()));
			Double width = Math.abs(p1.getX() - p2.getX());
			Double height = Math.abs(p1.getY() - p2.getY());
			currentShape = model.getShape(model.getSize()-1);
			Rectangle r = (Rectangle)currentShape;
			r.setHeight(height);
			r.setWidth(width);
			r.setUpperLeft(upLeft);
			model.deleteShape(model.getSize()-1);
			model.addShape(r);
		}else if(shape == "circle"){
			double size = Math.min((Math.abs(p1.getX()-p2.getX())),(Math.abs(p1.getY()-p2.getY())));
			double radius = size/2;
			Point2D.Double upLeft = new Point2D.Double();
			
			if(p1.getX() <= p2.getX() && p1.getY() <= p2.getY()){
				upLeft.setLocation(p1.getX(), p1.getY());
			}else if(p1.getX() <= p2.getX() && p1.getY() > p2.getY()){
				upLeft.setLocation(p1.getX(), p1.getY()-size);
			}else if(p1.getX() > p2.getX() && p1.getY() <= p2.getY()){
				upLeft.setLocation(p1.getX()-size, p1.getY());
			}else{
				upLeft.setLocation(p1.getX()-size, p1.getY()-size);
			}
			Point2D.Double center = new Point2D.Double();
			center.setLocation(upLeft.getX()+radius, upLeft.getY()+radius);
			currentShape = model.getShape(model.getSize()-1);
			Circle c = (Circle)currentShape;
			c.setCenter(center);
			c.setRadius(radius);
			model.deleteShape(model.getSize()-1);
			model.addShape(c);
		}else if(shape == "ellipse"){
			Point2D.Double center = new Point2D.Double();
			center.setLocation((p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2);
			Double width = Math.abs(p1.getX() - p2.getX());
			Double height = Math.abs(p1.getY() - p2.getY());
			currentShape = model.getShape(model.getSize()-1);
			Ellipse el = (Ellipse)currentShape;
			el.setCenter(center);
			el.setHeight(height);
			el.setWidth(width);
			model.deleteShape(model.getSize()-1);
			model.addShape(el);
		}else{
			
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void colorButtonHit(Color c) {
		col = c;
		GUIFunctions.changeSelectedColor(c);
	}

	@Override
	public void lineButtonHit() {
		shape = "line";
		triangleCount = 0;
	}

	@Override
	public void squareButtonHit() {
		shape = "square";
		triangleCount = 0;
	}

	@Override
	public void rectangleButtonHit() {
		shape = "rectangle";
		triangleCount = 0;
	}

	@Override
	public void circleButtonHit() {
		shape = "circle";
		triangleCount = 0;
	}

	@Override
	public void ellipseButtonHit() {
		shape = "ellipse";
		triangleCount = 0;
	}

	@Override
	public void triangleButtonHit() {
		shape = "triangle";
		triangleCount = 0;
	}

	//Lab 2
	@Override
	public void selectButtonHit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zoomInButtonHit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zoomOutButtonHit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hScrollbarChanged(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vScrollbarChanged(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openScene(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggle3DModelDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openImage(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveImage(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub
		
	}

	//Lab1
	@Override
	public void saveDrawing(File file) {
		model.save(file);
	}

	@Override
	public void openDrawing(File file) {
		model.open(file);
	}

	//Lab2
	@Override
	public void doDeleteShape() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doEdgeDetection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGrayscale() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMoveForward() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMoveBackward() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSendToFront() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSendtoBack() {
		// TODO Auto-generated method stub
		
	}

	public void setModel(MyModel model2) {
		model = model2;
	}

	public Shape getShape() {
		return currentShape;
	}
}
