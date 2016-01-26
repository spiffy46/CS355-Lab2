package cs355.controller;

import java.awt.Color;
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
	public String button = "";
	public Point2D.Double p1;
	public Point2D.Double p2;
	public int triangleCount = 0;
	public Point2D.Double t1;
	public Point2D.Double t2;
	public Point2D.Double t3;
	public Point2D.Double diff;
	public int selectedIndex = -1;

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		p1 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
		if (button == "line"){
			currentShape = new Line(col,p1,p1);
		}else if(button == "square"){
			currentShape = new Square(col,p1,0);
		}else if(button == "rectangle"){
			currentShape = new Rectangle(col,p1,0,0);
		}else if(button == "circle"){
			currentShape = new Circle(col,p1,0);
		}else if(button == "ellipse"){
			currentShape = new Ellipse(col,p1,0,0);
		}else if (button == "triangle"){
			if (triangleCount == 0){
				t1 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
				triangleCount++;
			}else if(triangleCount ==1){
				t2 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
				triangleCount++;
			}else{
				t3 = new Point2D.Double(e.getPoint().getX(),e.getPoint().getY());
				Point2D.Double center = new Point2D.Double();
				center.setLocation((t1.getX()+t2.getX()+t3.getX())/3, (t1.getY()+t2.getY()+t3.getY())/3);
				triangleCount = 0;
				Shape t = new Triangle(col,center,t1,t2,t3);
				model.addShape(t);
			}	
			return;
		}else if (button == "select"){
			selectedIndex = model.geometryTest(e.getPoint(), 4);
			if (selectedIndex > -1){
				GUIFunctions.changeSelectedColor(model.getShape(selectedIndex).getColor());
				diff.setLocation(e.getX()-model.getShape(selectedIndex).getCenter().getX(), e.getY()-model.getShape(selectedIndex).getCenter().getY());
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
		
		if (button == "line"){
			currentShape = model.getShape(model.getSize()-1);
			Line l = (Line)currentShape;
			l.setEnd(p2);
			model.deleteShape(model.getSize()-1);
			model.addShape(l);
		}else if(button == "square"){
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
			Point2D.Double center = new Point2D.Double();
			center.setLocation(upLeft.getX()+size/2, upLeft.getY()+size/2);
			currentShape = model.getShape(model.getSize()-1);
			Square s = (Square)currentShape;
			s.setCenter(center);
			s.setSize(size);
			model.deleteShape(model.getSize()-1);
			model.addShape(s);
		}else if(button == "rectangle"){
			Point2D.Double upLeft = new Point2D.Double();
			upLeft.setLocation(Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(),p2.getY()));
			Double width = Math.abs(p1.getX() - p2.getX());
			Double height = Math.abs(p1.getY() - p2.getY());
			currentShape = model.getShape(model.getSize()-1);
			Rectangle r = (Rectangle)currentShape;
			Point2D.Double center = new Point2D.Double();
			center.setLocation(upLeft.getX()+width/2, upLeft.getY()+height/2);
			r.setHeight(height);
			r.setWidth(width);
			r.setCenter(center);
			model.deleteShape(model.getSize()-1);
			model.addShape(r);
		}else if(button == "circle"){
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
		}else if(button == "ellipse"){
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
		}else if(button == "select" && selectedIndex > -1){
			Shape s = model.getSelectedShape();
			Point2D.Double center = s.getCenter();
			Point2D.Double newCenter = new Point2D.Double(center.getX() + (e.getX()-diff.getX()), center.getY() + (e.getY()-diff.getY()));
			s.setCenter(newCenter);
			model.setShape(selectedIndex, s);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void colorButtonHit(Color c) {
		col = c;
		if (selectedIndex > -1){
			Shape s = model.getShape(selectedIndex);
			s.setColor(col);
			model.setShape(selectedIndex, s);
		}
		GUIFunctions.changeSelectedColor(c);
	}

	@Override
	public void lineButtonHit() {
		button = "line";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
	}

	@Override
	public void squareButtonHit() {
		button = "square";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
	}

	@Override
	public void rectangleButtonHit() {
		button = "rectangle";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
	}

	@Override
	public void circleButtonHit() {
		button = "circle";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
	}

	@Override
	public void ellipseButtonHit() {
		button = "ellipse";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
	}

	@Override
	public void triangleButtonHit() {
		button = "triangle";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
	}

	@Override
	public void selectButtonHit() {
		button = "select";
		triangleCount = 0;
		selectedIndex = -1;
		model.setSelectedShape(selectedIndex);
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
		if(selectedIndex > -1){
			model.deleteShape(selectedIndex);
		}
		selectedIndex = -1;
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
		if (selectedIndex > -1 && selectedIndex != model.getShapes().size()-1){
			model.moveForward(selectedIndex);
			selectedIndex++;
		}
	}

	@Override
	public void doMoveBackward() {
		if (selectedIndex > -1 && selectedIndex != 0){
			model.moveBackward(selectedIndex);
			selectedIndex--;
		}
	}

	@Override
	public void doSendToFront() {
		// TODO Auto-generated method stub
		if (selectedIndex > -1){
			model.moveToFront(selectedIndex);
			selectedIndex = model.getShapes().size()-1;
		}
	}

	@Override
	public void doSendtoBack() {
		// TODO Auto-generated method stub
		if (selectedIndex > -1){
			model.movetoBack(selectedIndex);
			selectedIndex = 0;
			
		}
	}

	public void setModel(MyModel model2) {
		model = model2;
	}

	public Shape getShape() {
		return currentShape;
	}
}
