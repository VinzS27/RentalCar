package com.si2001.rentalcar.DAO;

import java.util.Date;

import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.si2001.rentalcar.model.PersistentLogin;

@Repository("tokenRepositoryDao")
@Transactional
public class HibernateTokenRepositoryImpl extends AbstractDao<String, PersistentLogin>
        implements PersistentTokenRepository {

    static final Logger logger = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        logger.info("Creating Token for user : {}", token.getUsername());
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        persist(persistentLogin);

    }

    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        logger.info("Fetch Token if any for seriesId : {}", seriesId);
        try {
            PersistentLogin persistentLogin = (PersistentLogin) getEntityManager()
                    .createQuery("SELECT p FROM PersistentLogin p WHERE p.series LIKE :seriesId")
                    .setParameter("seriesId", seriesId)
                    .getSingleResult();

            return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
                    persistentLogin.getToken(), persistentLogin.getLast_used());
        } catch (Exception e) {
            logger.info("Token not found...");
            return null;
        }
    }


    public void removeUserTokens(String username) {
        PersistentLogin persistentLogin = null;
        logger.info("Removing Token if any for user : {}", username);
        try {
            persistentLogin = (PersistentLogin) getEntityManager()
                    .createQuery("SELECT p FROM PersistentLogin p WHERE p.username LIKE :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException noR) {

        }

        if (persistentLogin != null) {
            logger.info("rememberMe was selected");
            delete(persistentLogin);
        }

    }

    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        logger.info("Updating Token for seriesId : {}", seriesId);
        PersistentLogin persistentLogin = getByKey(seriesId);
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        update(persistentLogin);
    }

}
