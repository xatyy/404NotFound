package ro.notfound.co_gui;

import com.sun.management.OperatingSystemMXBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Scanner;

public class RAM_SpecsController {
    private Stage stage;
    private Scene scene;
    @FXML
    private Label myLabelProcessor ;
    @FXML
    private Label myLabelOS ;
    @FXML
    private Label myLabelMemory ;
    @FXML
    private Label myLabelUsername;
    @FXML
    protected void switchtoRAM(ActionEvent TO_RAM) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RAM_Scene.fxml"));
        stage = (Stage) ((Node)TO_RAM.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    protected void go_Back(ActionEvent go_back) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        stage = (Stage) ((Node)go_back.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void History(ActionEvent history) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("History_RAM.fxml"));
        stage = (Stage) ((Node) history.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void initialize(){
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean processorname = ManagementFactory.getRuntimeMXBean();
        Runtime memory = Runtime.getRuntime();
        OperatingSystemMXBean operatingSystemBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory;
        totalPhysicalMemory = operatingSystemBean.getTotalPhysicalMemorySize();
        long totalRAMInGB = (totalPhysicalMemory / (1024 * 1024 * 1024));

        String userName = System.getProperty("user.name");

        String text = getCpuModel();
        String text2 = osBean.getName() + " "+ osBean.getVersion();
        String text3 = totalRAMInGB + " GB RAM";
        String text4 = userName;


        myLabelOS.setText(text2);
        myLabelProcessor.setText(text);
        myLabelMemory.setText(text3);
        myLabelUsername.setText(text4);

    }
    public String getCpuModel() {
        String osName = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        String cpuModelWindows = "Unknown";

        if (osName.contains("win")) { // Windows
            try {
                Process process = Runtime.getRuntime().exec("wmic cpu get name");
                Scanner scanner = new Scanner(process.getInputStream());
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty() && !line.equalsIgnoreCase("name")) {
                        cpuModelWindows = line;
                        break;
                    }
                }
                scanner.close();
                return  cpuModelWindows.trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (osName.contains("mac")) { // macOS
            try {
                Process process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string");
                Scanner scanner = new Scanner(process.getInputStream());
                String cpuModel = scanner.nextLine();
                scanner.close();
                return cpuModel.trim();
            } catch (IOException e) {
                e.printStackTrace();
                return "Unknown";
            }
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) { // Linux/Unix
            try {
                Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo | grep 'model name' | uniq");
                Scanner scanner = new Scanner(process.getInputStream());
                String cpuModel = scanner.nextLine();
                scanner.close();
                return cpuModel.substring(cpuModel.indexOf(':') + 1).trim();
            } catch (IOException e) {
                e.printStackTrace();
                return "Unknown";
            }
        } else { // Unsupported OS
            return "Unknown";
        }

        return "Unknown";
    }


}