SELECT 
 title FROM groups
 WHERE
  /*if(GROUP_NAME)start*/
  title LIKE GROUP_NAME
  /*if(GROUP_NAME)end*/
 ;