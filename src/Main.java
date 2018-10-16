
/**
 * @author Webro
 *
 */
import java.util.*;
class JueSe {
    private int HP;
    private int AK;
    public JueSe next;

    public JueSe(int ak, int hp) {
        HP = hp;
        AK = ak;
        next = null;
    }

    public int getHP() {
        return HP;
    }

    /**
     * @param x 被攻击者
     */
    public void attack(JueSe x) {
        x.defend(AK);
        this.defend(x.AK);
    }

    public void defend(int ak) {
        HP -= ak;
    }
}
/**
 *  定义玩家类
 *
 */
class player {
    private JueSe head;
    private int num;

    public player() {
        num = 0;
        head = new JueSe(0, 30);
    }

    /**
     * 判断角色是否死亡，死亡则删除该角色;如果英雄死亡返回1；否则返回0；
     * @param position 随从位置
     *
     */
    private int JianCeSiWang(int position) {
        JueSe ZanShi = head;
        for (int i = 0; i < position - 1; i++) {
            ZanShi = ZanShi.next;
        }
        if (position > 0) {
            if (ZanShi.next.getHP() <= 0) {
                ZanShi.next = ZanShi.next.next;
                num--;
            }
        } else {
            if (head.getHP() <= 0)
                return 1;
        }
        return 0;
    }

    /**
     * 新增随从
     * @param position 新增随从的位置
     * @param ak 攻击力
     * @param hp 血量
     */
    public void summon(int position, int ak, int hp) {
        JueSe p = head, ZanShi = null;//零时变量，存储下一next
        for (int i = 0; i < position - 1; i++) {
            p = p.next;
        }
        ZanShi = p.next;
        p.next = new JueSe(ak, hp);
        p = p.next;
        p.next = ZanShi;
        num++;//计数+1
    }
/**
 * 攻击操作，最后判断角色是否死亡,英雄死亡返回1，存活返回0
 * @param Attacker 攻击者
 * @param Defender 被攻击者
 * @param other 对方玩家
 * @return
 */
    public int attack(int Attacker, int Defender, player other) {
        JueSe atk = head, def = other.head;
        for (int i = 0; i < Attacker; i++) {
            atk = atk.next;
        }
        for (int i = 0; i < Defender; i++) {
            def = def.next;
        }
        atk.attack(def);

        JianCeSiWang(Attacker);
        return other.JianCeSiWang(Defender);
    }


    /**
     * 格式化输出
     */
    public void display() {
        JueSe p = head;
        System.out.println(head.getHP());
        System.out.print(num);
        for (int i = 0; i < num; i++) {
            p = p.next;
            System.out.print(" " + p.getHP());
        }
        System.out.println();
    }
}

/**
 * 战场类，用于整合两位玩家的操作
 */
class ZhanChang {
    private player[] Player;
    private int winner;

    ZhanChang() {
        winner = 0;
        Player = new player[]{new player(),new player()};
    }

    /**
     * 操作方法，通过键盘输入
     */
    public void operator() {
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        int posi, ak, hp, attacker, defender;
        String operation;
        //for循环中i用于统计操作次数，j用于改变操作对象（玩家）（j%2的值为0或1）
        for (int i = 0, j = 0; i < n; i++) {
            operation = reader.next();

            if (operation.equals("summon")) {
                posi = reader.nextInt();
                ak = reader.nextInt();
                hp = reader.nextInt();
                Player[j%2].summon(posi, ak, hp);

            } else if (operation.equals("attack")) {
                attacker = reader.nextInt();
                defender = reader.nextInt();
                //进行攻击操作，同时判断胜负，此时只可能另一位玩家死亡或存活，当前玩家不可能死亡，故当前玩家胜利或平局。
                if (Player[j % 2].attack(attacker, defender, Player[(j + 1) % 2]) == 1) {
                    if (j % 2 == 0)//先手玩家
                        winner = 1;
                    else winner = -1;//后手玩家

                }
            } else if (operation.equals("end")) {
                j++;//回合结束，切换玩家操作
            }
        }
    }

    /**
     * 格式化输出
     */
    public void display() {
        System.out.println(winner);
        Player[0].display();
        Player[1].display();
    }
}

public class Main {
    public static void main(String[] args) {
        ZhanChang one = new ZhanChang();
        one.operator();
        one.display();
    }
}