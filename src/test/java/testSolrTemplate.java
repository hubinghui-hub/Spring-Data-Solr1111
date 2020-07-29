import com.offcn.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-solr.xml")

public class testSolrTemplate {

    @Autowired
    private SolrTemplate solrTemplate;

@Test
    public void SolrAdd(){
        TbItem item = new TbItem();
        item.setId(4L);
        item.setBrand("小米为");
        item.setCategory("手机pluse");
        item.setGoodsId(1L);
        item.setSeller("小米1号专卖店");
        item.setTitle("红米Mate9");
        item.setPrice(new BigDecimal(2200));
        solrTemplate.saveBean(item);//插入到slor库
        solrTemplate.commit();//提交事务

    }


    @Test
    public void findOne(){

        TbItem item = solrTemplate.getById(4, TbItem.class);
        System.out.println(item.getTitle());



    }


    @Test
    public void deleteById(){
    Query q = new SimpleQuery("*:*");
        solrTemplate.delete(q);
        solrTemplate.commit();


    }
    @Test
    public void addTbItemList(){
        List<TbItem> list=new ArrayList();
        for (int i = 1; i <101 ; i++) {
            TbItem item=new TbItem();
            item.setId(Long.valueOf(i));
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为"+i+"号专卖店");
            item.setTitle("华为Mate"+i);
            item.setPrice(new BigDecimal(2000+i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();


    }


    @Test
    public void testPageQuery(){
        Query query = new SimpleQuery("*:*");//查询全部
        query.setOffset(0);//从零索引开始
        query.setRows(2);//每页显示条数
        ScoredPage<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总记录数：" + items.getTotalElements());
        for (TbItem item : items) {
            System.out.println(item.getTitle());
        }


    }

    @Test
    public void pageQuery(){

        Query query = new SimpleQuery("*:*");//查询全部
        Criteria criteria = new Criteria("item_title").contains("8");//条件是标题包含8的

        criteria = criteria.and("item_price").greaterThan(2050);//条件价格大于2010的
        query.addCriteria(criteria);//把条件装进查询对象
        Sort s = new Sort(Sort.Direction.DESC, "item_price");
        query.addSort(s);//排序
        ScoredPage<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);//查询solrd的分页，条件对象和查询对象装
        List<TbItem> list = items.getContent();
        for (TbItem item : list) {
            System.out.println(item.getTitle()+"价格："+item.getPrice());

        }


    }


}
