SELECT
 MAX(line)
 FROM
  groups_detail
  WHERE
 /*if(USER_ID)start*/
 id = USER_ID
 /*if(USER_ID)end*/
 ;