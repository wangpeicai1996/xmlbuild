package even.mapper;

import even.enetity.Blog;

public interface BlogMapper {

    public Blog selectById(Integer id);
    public Blog selectByName(String name);
}
