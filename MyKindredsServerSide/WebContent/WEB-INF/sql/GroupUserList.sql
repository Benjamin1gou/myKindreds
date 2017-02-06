SELECT 
 groups.title
 FROM groups
 INNER JOIN
 groups_detail
 ON
 groups.id = groups_detail.id
 WHERE
 /*if(USER_ID)start*/
 groups_detail.user_id = USER_ID
 /*if(USER_ID)end*/
;
