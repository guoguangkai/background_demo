package com.zgx.ademo.service;

import com.zgx.ademo.dao.AdminMenuDAO;
import com.zgx.ademo.entity.AdminMenu;
import com.zgx.ademo.entity.AdminRoleMenu;
import com.zgx.ademo.entity.AdminUserRole;
import com.zgx.ademo.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    public List<AdminMenu> getAllByParentId(int parentId) {return adminMenuDAO.findAllByParentId(parentId);}
    //根据当前用户查询出所有菜单项
    public List<AdminMenu> getMenusByCurrentUser() {
        //shiro里的主体(Principal) 登录成功后主体为用户对象
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);
        List<AdminUserRole> userRoleList = adminUserRoleService.listAllByUid(user.getId());
        List<AdminMenu> menus = new ArrayList<>();
        for (AdminUserRole userRole : userRoleList) {
            List<AdminRoleMenu> rms = adminRoleMenuService.findAllByRid(userRole.getRid());
            for (AdminRoleMenu rm : rms) {
                // 增加防止多角色状态下菜单重复的逻辑
                AdminMenu menu = adminMenuDAO.findById(rm.getMid());
                boolean isExist = false;
                for (AdminMenu m : menus) {
                    if (m.getId() == menu.getId()) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    menus.add(menu);
                }
            }
        }
        handleMenus(menus);
        return menus;
    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<AdminMenu> menus = new ArrayList<>();
        List<AdminRoleMenu> rms = adminRoleMenuService.findAllByRid(rid);
        for (AdminRoleMenu rm : rms) {
            menus.add(adminMenuDAO.findById(rm.getMid()));
        }
        handleMenus(menus);
        return menus;
    }
    //把查询出来的菜单数据列表整合成具有层级关系的菜单树
    public void handleMenus(List<AdminMenu> menus) {
        for (AdminMenu menu : menus) {
            //由于导航菜单一般不会特别长，所以我们采用这种一次性取出的方式。上述过程中我们会在遍历列表的同时查询数据库，这样的多次交互在前台需要尽量避免，
            // 最好先一次性查询出全量数据以减轻服务器负担。但后台一般是给管理人员使用的，没有那么大的流量，所以不用担心。
            //如果数据量特别大，那就应该考虑按节点动态加载。即通过监听节点的展开事件向后端发送节点 id 作为参数，查询出所有的子节点，并在前端动态渲染。
            List<AdminMenu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        }
/*        为什么删除子项时用 iterator.remove() 而不用 List 的 remove 方法呢？是因为使用 List 遍历时，如果删除了某一个元素，后面的元素会补上来，
        也就是说后面元素的索引和列表长度都会发生改变。而循环仍然继续，循环的次数仍是最初的列表长度，这样既会漏掉一些元素，又会出现下标溢出，
        运行时表现就是会报 ConcurrentModificationException。而 iterator.remove() 进行了一些封装，会把当前索引和循环次数减 1，从而避免了这个问题。*/
       /* Iterator<AdminMenu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            AdminMenu menu = iterator.next();
            if (menu.getParentId() != 0) {
                iterator.remove();
            }
        }*/
       //JDK 8 以上版本推荐使用一种简化的方式进行迭代判断：使用 removeIf
        menus.removeIf(menu -> menu.getParentId() != 0);
    }
}
