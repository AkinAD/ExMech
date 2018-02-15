package enamel;

public class Main {

	public static void main(String[] args) {
		View view = new View();
		Controller controller = new Controller(view);
		view.setController(controller);
		view.init();
		view.frame.setVisible(true);
	}

}
