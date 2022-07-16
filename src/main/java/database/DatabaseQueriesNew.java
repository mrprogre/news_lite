package database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class DatabaseQueriesNew {
    ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
    JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

    // вставка кода по заголовку для отсеивания ранее обнаруженных новостей
    public void insertTitleIn256(String pTitle) {
        String query = "INSERT INTO titles256(title) VALUES (?)";
        jdbcTemplate.update(query, pTitle);
    }

    // сохранение всех заголовков
    public void insertAllTitles(String pTitle, String pDate) {
        String query = "INSERT INTO ALL_NEWS(TITLE, NEWS_DATE) VALUES (?, ?)";
        jdbcTemplate.update(query, pTitle, pDate);
    }

    // отсеивание заголовков
    public boolean isTitleExists(String pString256) {
        String query = "SELECT MAX(1) FROM TITLES256 WHERE EXISTS (SELECT TITLE FROM TITLES256 T WHERE T.TITLE = ?)";
        Integer isExists = jdbcTemplate.queryForObject(query, Integer.class, pString256);
        return isExists != null;
    }
}
