package NewTest;

import java.util.ArrayList;
import java.util.List;

//* @Author dw
//* @ClassName Menu
//* @Description
//* @Date 2021/11/30 17:35
//* @Version 1.0
//*/
//@Data
public class Menu {
   private String menuId;
   private String parentId;
   private String menuDescribe;
   private String url;
   private List<Menu> children;

   public Menu(String menuId, String parentId, String menuDescribe, String url) {
       this.menuId = menuId;
       this.parentId = parentId;
       this.menuDescribe = menuDescribe;
       this.url = url;
   }
   /*省略get\set*/
}
/**
 * @Author dw
 * @ClassName MenuTree
 * @Description
 * @Date 2021/11/30 17:54
 * @Version 1.0
 */
