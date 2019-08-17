package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.scene.layout.GridPane;

class ButtonGenerator{                                                         // Generate button and set the position of the button
	public Button button = new Button();
	public int x,y;
	
	public void setposition(int x, int y){                                    // store position
		this.x = x;
		this.y = y;
	}
	
	public Button getButton() {                                              // return button
		return button;
	}
	
	public int getx() {                                                     // return position
		return x;
	}
	
	public int gety() {                                                    // return position
		return y;
	}
}



public class Main extends Application {
	
	FileChooser fileChooser = new FileChooser();
	Image image, reimage;
	PixelReader pixelReader;                                                  // read the pixel of image
	WritableImage writableImage;                                              // to change the color of image
	PixelWriter pixelWriter;                                                  // write the color to the image
	BorderPane boardPane = new BorderPane();
	GridPane boardGrid =  FinitBoard();                                       // to show the image
	ColorPicker colorpicker = new ColorPicker();
	
	
	
	private GridPane initBoard(int wid, int len) {                            // create grid pane and set the image to the pane
		GridPane boardGrid = new GridPane();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG", "*.png")
				);
		for(int i = 0; i < wid; i++) {
			for(int j = 0; j < len; j++) {
				ButtonGenerator btg =  new ButtonGenerator();
				 btg.setposition(i, j);                                      // set button size
				 Color color = pixelReader.getColor(i,j);                    // read color
				 pixelWriter.setColor(i, j, color);
				 String c1 = String.format("#%02x%02x%02x",
						 (int)(color.getRed()*255),(int)(color.getGreen()*255), (int)(color.getBlue()*255));
				 btg.getButton().setStyle("-fx-background-radius: 0; -fx-border-radius: 0;"+"-fx-background-color:" + c1);   // set button style
				 btg.getButton().setOnAction((e) -> {
					 Color colorp = colorpicker.getValue();                  // get color from color picker
					 pixelWriter.setColor(btg.getx(), btg.gety(), colorp);   // set color to the image
					 String c2 = String.format("#%02x%02x%02x", 
							 (int)(colorp.getRed()*255), (int)(colorp.getGreen()*255), (int)(colorp.getBlue()*255));
					 btg.getButton().setStyle("-fx-background-radius: 0; -fx-border-radius: 0;"+"-fx-background-color:" + c2);
					  
				 });
				 btg.getButton().setPrefSize(wid, len);
				 btg.getButton().setMaxSize(25, 25);
				 btg.getButton().setMinSize(1, 1);
				 boardGrid.add(btg.getButton(),i,j);                      // add button to the pane                    
			}
		}
		boardGrid.setAlignment(Pos.CENTER);                               // set boardGrid to the center
		return boardGrid;
	}
	

	private GridPane FinitBoard(){                                            // Initialize the panel with a 32X32 white board
		image = new Image("application/draft.png");
		reimage= image;
		pixelReader = image.getPixelReader();
		writableImage = new WritableImage( (int)image.getWidth(), (int)image.getHeight());
		pixelWriter = writableImage.getPixelWriter();
		boardGrid = initBoard((int)image.getWidth(),(int)image.getHeight());  // set the size of boardGrid
		return boardGrid;
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			HBox hbox = new HBox();                                            //  to display button
			Button load = new Button("Load");                                  //  set load button 
			load.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
			load.setOnAction(new EventHandler<ActionEvent>() {                 //  click the button and open image file
				@Override
				public void handle(ActionEvent arg0) {
					File file =  fileChooser.showOpenDialog(primaryStage);
					String fileUrl = file.toURI().toString();                  // get file path
					if (file != null) {
						image = new Image(fileUrl);
						reimage= image;
						pixelReader = image.getPixelReader();
						writableImage = new WritableImage( (int)image.getWidth(), (int)image.getHeight());
						pixelWriter = writableImage.getPixelWriter();
						GridPane boardGrid = initBoard((int)image.getWidth(),(int)image.getHeight());
						boardPane.setCenter(boardGrid);
					}
				}
			});
			
			boardPane.setCenter(boardGrid);
			colorpicker.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
			
			Button reset = new Button("Reset");                        // reset the current image
			reset.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
			reset.setOnAction(new EventHandler<ActionEvent>() {        // click button and initialize
				@Override  
				public void handle(ActionEvent event) {  
					pixelReader = reimage.getPixelReader();
					writableImage = new WritableImage( (int)reimage.getWidth(), (int)reimage.getHeight());
					pixelWriter = writableImage.getPixelWriter();
					boardGrid = initBoard((int)reimage.getWidth(),(int)reimage.getHeight());
					boardPane.setCenter(boardGrid);
					primaryStage.show();
				}
			});
			
			
			Button init = new Button("Initialization");               // Initialize the panel with a 32-sized white board
			init.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
			init.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					boardGrid = FinitBoard();
					boardPane.setCenter(boardGrid);
	            	primaryStage.show();
				}
			});
			
			
			Button save = new Button("Save");                        // save image
			save.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
			save.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) { 
					File file = fileChooser.showSaveDialog(primaryStage.getOwner());                         // get file
					if (file != null) {
						try { 
							ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);       // save file as .png
						}catch (IOException e) {
							 e.printStackTrace();
						}
					}
				}
			});
			
			hbox.getChildren().addAll(init,load,colorpicker,reset,save);                        // add button to the hbox
			hbox.setAlignment(Pos.CENTER);
			HBox.setHgrow(init, Priority.ALWAYS);
			HBox.setHgrow(load, Priority.ALWAYS);
			HBox.setHgrow(colorpicker, Priority.ALWAYS);
			HBox.setHgrow(reset, Priority.ALWAYS);
			HBox.setHgrow(save, Priority.ALWAYS);
			boardPane.setTop(hbox);
			primaryStage.setTitle("Sprite Editor");
			primaryStage.setScene(new Scene(boardPane));
			primaryStage.setMaximized(true);
			primaryStage.show();	

		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

