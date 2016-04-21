package com.feamor.beauty.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Home on 27.02.2016.
 */

@Entity(name = "page")
@Table(name = "page")
public class Page {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "page_id_generator", sequenceName = "page_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_id_generator")
    private int id;

    @Column(name = "alias", length = 200)
    private String alias;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_type")
    private PageType pageType;

    @Column(name = "comment", length = 1000)
    private String comment;

    @OneToMany(mappedBy = "page", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PageBlock> blocks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PageBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<PageBlock> blocks) {
        this.blocks = blocks;
    }
}
