package viewer;

public class ApplicationRunner {
    public static void main(String[] args) {
        new SQLiteViewer().setVisible(true);

//        new DataBaseAdapter() {{
//            OpenDataSource("jdbc:sqlite:first.db");
//            InitDatabase1();
//        }};
//
//        new DataBaseAdapter() {{
//            OpenDataSource("jdbc:sqlite:second.db");
//            InitDatabase2();
//        }};
    }
}
