package com.feamor.beauty.tests;

import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.models.BlockType;
import com.feamor.beauty.models.Page;
import com.feamor.beauty.models.PageBlock;
import com.feamor.beauty.models.PageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Home on 13.02.2016.
 */
@Transactional
@RestController
public class TestController {

    @Autowired
    private PageDao pageDao;

    @RequestMapping(value = "/", produces = "text/html;charset=utf-8")
    @ResponseBody
    String home() {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><meta charset=\"utf-8\"><body><h1>Список типов страниц</h1>");

        List<PageType> pageTypes = pageDao.listPageTypes();
        if (pageTypes == null || pageTypes.size() == 0) {
            builder.append("нет ни однеого типа страниц");
        } else {
            builder.append("<table border=\"1\"><tr><th>Id</th><th>Alias</th><th>ClassId</th><th>Description</th></tr>");
            for(PageType pageType : pageTypes) {
                builder.append("<tr><td>"+pageType.getId()+"</td><td>"+pageType.getAlias()+"</td><td>"+pageType.getClassId()+"</td><td>"+pageType.getDescription()+"</td></tr>");
            }
            builder.append("</table>");
        }
        builder.append("</body><html>");
        return builder.toString();
    }

    @RequestMapping(value = "/pages", produces = "text/html;charset=utf-8")
    @ResponseBody
    String pages() {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><meta charset=\"utf-8\"><body><h1>Список Страниц</h1>");

        List<Page> pages = pageDao.listPages();
        if (pages == null || pages.size() == 0) {
            builder.append("нет ни одной страницы");
        } else {
            builder.append("<table border=\"1\"><tr><th>Id</th><th>Alias</th><th>Comment</th><th>page type</th><th>Blocks count</th></tr>");
            for(Page page : pages) {

                builder.append("<tr><td>"+page.getId()+"</td><td>"+page.getAlias()+"</td><td>"+page.getComment()+"</td>");

                PageType pageType = page.getPageType();
                if (pageType != null) {
                    builder.append("<td>"+pageType.getAlias()+"</td>");
                } else {
                    builder.append("<td> unknown </td>");
                }

                List<PageBlock> blocks = page.getBlocks();
                if (blocks!= null && blocks.size()>0) {
                    builder.append("<td>" + page.getBlocks().size() + "</td>");
                } else {
                    builder.append("<td>Нет блоков</td>");
                }

                builder.append("</tr>");
            }
            builder.append("</table>");
        }
        builder.append("</body><html>");
        return builder.toString();
    }

    @RequestMapping(value = "/block_types", produces = "text/html;charset=utf-8")
    @ResponseBody
    String blockTypes() {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><meta charset=\"utf-8\"><body><h1>Список типов блоков</h1>");

        List<BlockType> blockTypes = pageDao.listBlockTypes();
        if (blockTypes == null || blockTypes.size() == 0) {
            builder.append("нет ни одного типа блоков");
        } else {
            builder.append("<table border=\"1\"><tr><th>Id</th><th>Alias</th><th>ClassId</th><th>Comment</th></tr>");
            for(BlockType blockType : blockTypes) {
                builder.append("<tr><td>"+blockType.getId()+"</td><td>"+blockType.getAlias()+"</td><td>"+blockType.getClassId()+"</td><td>"+blockType.getDescription()+"</td></tr>");
            }
            builder.append("</table>");
        }
        builder.append("</body><html>");
        return builder.toString();
    }

    @RequestMapping(value = "/pageBlocks", produces = "text/html;charset=utf-8")
    @ResponseBody
    String blocksForPage(@RequestParam(name = "page", required = false) Integer pageId) {
        StringBuilder builder = new StringBuilder();

        if (pageId != null) {
            builder.append("<html><meta charset=\"utf-8\"><body><h1>Список блоков для страницы #" + pageId + "</h1>");

            List<PageBlock> blocks = pageDao.listPageBlocks(pageId);
            if (blocks == null || blocks.size() == 0) {
                builder.append("нет ни одного блокоа");
            } else {
                builder.append("<table border=\"1\"><tr><th>Id</th><th>Block type</th><th>ClassId</th><th>Position</th></tr>");
                for (PageBlock block : blocks) {
                    builder.append("<tr><td>" + block.getId() + "</td><td>" + block.getBlockType().getAlias() + "</td><td>" + block.getPosition() + "</td></tr>");
                }
                builder.append("</table>");
            }
        } else {
            builder.append("<h1>Параметр страницы не указан</h1>");
        }
        builder.append("</body><html>");
        return builder.toString();
    }

    @RequestMapping(value = "/list_blocks", produces = "text/html;charset=utf-8")
    @ResponseBody
    String listAllBlocks() {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><meta charset=\"utf-8\"><body><h1>Список блоков</h1>");

        List<PageBlock> blocks = pageDao.listPageBlocks();
        if (blocks == null || blocks.size() == 0) {
            builder.append("нет ни одного блокоа");
        } else {
            builder.append("<table border=\"1\"><tr><th>Id</th><th>Block type</th><th>ClassId</th><th>Position</th></tr>");
            for(PageBlock block : blocks) {
                builder.append("<tr><td>"+block.getId()+"</td><td>"+block.getBlockType().getAlias()+"</td><td>"+block.getPosition()+"</td></tr>");
            }
            builder.append("</table>");
        }
        builder.append("</body><html>");
        return builder.toString();
    }

}