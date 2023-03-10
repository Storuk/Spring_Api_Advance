package com.epam.esm.utils;

/**
 * Class with queries for repositories methods
 *
 * @author Vlad Storoshchuk
 */
public class SqlQueries {
    /**
     * Class with queries for Tag repository
     */
    public static class TagsQueries {
        public static final String GET_MOSTLY_USED_TAG = """
                select t.id, t.name from orders ors
                        inner join gift_certificate_tags gt
                        inner join tag t
                        where ors.gift_certificate_id = gt.gift_certificate_id
                        and t.id = gt.tags_id
                        and ors.user_id = (SELECT os.user_id FROM orders os,users us
                                           WHERE os.user_id = us.id
                                           GROUP BY os.user_id
                                           ORDER BY SUM(os.cost) DESC
                                           LIMIT 0, 1)
                group by t.id, t.name
                order by count(t.id) desc
                LIMIT 0, 1""";
        public static final String GET_TAG_ID_BY_TAG_NAME = "SELECT t.id FROM Tag t WHERE t.name = ?1";
    }

    /**
     * Class with queries for GiftCertificate repository
     */
    public static class GiftCertificatesQueries {
        public static final String GET_GIFT_CERTIFICATE_BY_PART_OF_DESCRIPTION = "SELECT * FROM gift_certificate WHERE description LIKE ?1 LIMIT ?2,?3";
        public static final String GET_GIFT_CERTIFICATE_BY_TAG_NAME = "" +
                "SELECT gc.id , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date " +
                "FROM gift_certificate_tags gt  " +
                "JOIN tag t " +
                "JOIN gift_certificate gc  " +
                "WHERE gc.id = gt.gift_certificate_id AND t.id = gt.tags_id AND t.name = ?1 " +
                "LIMIT ?2,?3";
        public static final String GET_ALL_GIFT_CERTIFICATES_BY_TAGS = """
                SELECT DISTINCT (gc.id) , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date
                FROM gift_certificate_tags gct\s
                JOIN tag t\s
                JOIN gift_certificate gc
                WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tags_id AND t.name IN (?1)
                group by gc.name, (gc.id), gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date
                having count(t.id) = ?2\s
                LIMIT ?3,?4""";
    }
}