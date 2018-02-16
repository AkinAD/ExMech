package enamel;

public class Main {

	public static void main(String[] args) {
		View view = new View();
		Controller controller = new Controller(view);
		controller.initTestList();
		view.setController(controller);
		view.init();
		view.frame.setVisible(true);
	}

}
