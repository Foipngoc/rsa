package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/16.
 */
@Repository
public class BaseDao
{
    @Resource
    SessionFactory sessionFactoryMySql;

    public void save(Object entity)
    {
        Session session=sessionFactoryMySql.openSession();
        Transaction transaction=session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
    }
}
