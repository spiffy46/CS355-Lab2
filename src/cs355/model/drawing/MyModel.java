package cs355.model.drawing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}
