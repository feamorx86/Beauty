package com.feamor.beauty.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Home on 27.02.2016.
 */
@Entity(name = "pageBlock")
@Table(name = "page_block")
public class PageBlock {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "page_block_id_generator", sequenceName = "page_block_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_block_id_generator")
    private int id;

    @Column(name = "position")
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_page")
    private Page page;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_block_type")
    private BlockType blockType;

    @OneToMany(mappedBy = "pageBlock", fetch = FetchType.LAZY)
    private List<BlockParameter> parameters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public List<BlockParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<BlockParameter> parameters) {
        this.parameters = parameters;
    }
}
