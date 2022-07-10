package _99_responsibility_chain_pattern.demo4;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2022/6/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 */


enum GatewayEnum {
    //枚举中应该维护的是类的全限定名，而非类的class文件。这里为了方便演示和后期包路径调整才这样写。
    // handlerId, 拦截者名称，全限定类名，preHandlerId，nextHandlerId
    API_HANDLER(new GatewayEntity(1, "api接口限流", ApiLimitGatewayHandler.class, null, 2)),
    BLACKLIST_HANDLER(new GatewayEntity(2, "黑名单拦截", BlacklistGatewayHandler.class, 1, 3)),
    SESSION_HANDLER(new GatewayEntity(3, "用户会话拦截", SessionGatewayHandler.class, 2, null)),
    ;

    GatewayEntity gatewayEntity;

    public GatewayEntity getGatewayEntity() {
        return gatewayEntity;
    }

    GatewayEnum(GatewayEntity gatewayEntity) {
        this.gatewayEntity = gatewayEntity;
    }
}

@Data
@AllArgsConstructor
class GatewayEntity {

    private Integer handlerId;

    private String name;

    private Class<?> entityClass;

    private Integer preHandlerId;

    private Integer nextHandlerId;
}


interface GatewayDao {

    /**
     * 根据 handlerId 获取配置项
     *
     * @param handlerId
     * @return
     */
    GatewayEntity getGatewayEntity(Integer handlerId);

    /**
     * 获取第一个处理者
     *
     * @return
     */
    GatewayEntity getFirstGatewayEntity();
}

class GatewayImpl implements GatewayDao {

    /**
     * 初始化，将枚举中配置的handler初始化到map中，方便获取
     */
    private static Map<Integer, GatewayEntity> gatewayEntityMap = new HashMap<>();

    static {
        GatewayEnum[] values = GatewayEnum.values();
        for (GatewayEnum value : values) {
            GatewayEntity gatewayEntity = value.getGatewayEntity();
            gatewayEntityMap.put(gatewayEntity.getHandlerId(), gatewayEntity);
        }
    }

    @Override
    public GatewayEntity getGatewayEntity(Integer handlerId) {
        return gatewayEntityMap.get(handlerId);
    }

    @Override
    public GatewayEntity getFirstGatewayEntity() {
        for (Map.Entry<Integer, GatewayEntity> entry : gatewayEntityMap.entrySet()) {
            GatewayEntity value = entry.getValue();
            //  没有上一个handler的就是第一个
            if (value.getPreHandlerId() == null) {
                return value;
            }
        }
        return null;
    }
}

abstract class GatewayHandler {
    /**
     * 下一关用当前抽象类来接收
     */
    protected GatewayHandler next;

    public void setNext(GatewayHandler next) {
        this.next = next;
    }

    abstract void hand();
}

class GatewayHandlerEnumFactory {

    private static GatewayDao gatewayDao = new GatewayImpl();

    // 提供静态方法，获取第一个handler
    public static GatewayHandler getFirstGatewayHandler() {

        GatewayEntity firstGatewayEntity = gatewayDao.getFirstGatewayEntity();
        GatewayHandler firstGatewayHandler = newGatewayHandler(firstGatewayEntity);
        if (firstGatewayHandler == null) {
            return null;
        }

        GatewayEntity tempGatewayEntity = firstGatewayEntity;
        Integer nextHandlerId = null;
        GatewayHandler tempGatewayHandler = firstGatewayHandler;
        // 迭代遍历所有handler，以及将它们链接起来
        while ((nextHandlerId = tempGatewayEntity.getNextHandlerId()) != null) {
            GatewayEntity gatewayEntity = gatewayDao.getGatewayEntity(nextHandlerId);
            GatewayHandler gatewayHandler = newGatewayHandler(gatewayEntity);
            tempGatewayHandler.setNext(gatewayHandler);
            tempGatewayHandler = gatewayHandler;
            tempGatewayEntity = gatewayEntity;
        }
        // 返回第一个handler
        return firstGatewayHandler;
    }

    /**
     * 反射实体化具体的处理者
     *
     * @param firstGatewayEntity
     * @return
     */
    private static GatewayHandler newGatewayHandler(GatewayEntity firstGatewayEntity) {
        // 获取全限定类名
        try {
            // 根据全限定类名，加载并初始化该类，即会初始化该类的静态段
            Class<?> clazz = firstGatewayEntity.getEntityClass();
            return (GatewayHandler) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class ApiLimitGatewayHandler extends GatewayHandler {

    @Override
    void hand() {
        System.out.println("限流");
        if (this.next != null) {
            this.next.hand();
        }
    }
}

class BlacklistGatewayHandler extends GatewayHandler {
    @Override
    void hand() {
        System.out.println("黑名单");
        if (this.next != null) {
            this.next.hand();
        }
    }
}

class SessionGatewayHandler extends GatewayHandler {
    @Override
    void hand() {
        System.out.println("session");
        if (this.next != null) {
            this.next.hand();
        }
    }
}


public class ResponsibilityChainImprove {
    public static void main(String[] args) {
        GatewayHandler firstGatewayHandler = GatewayHandlerEnumFactory.getFirstGatewayHandler();
        firstGatewayHandler.hand();
    }
}