package com.feamor.beauty.models;

import javax.persistence.*;

/**
 * Created by Home on 27.02.2016.
 */
@Entity(name = "blockParameter")
@Table(name = "block_parameters")
public class BlockParameter {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "block_parameter_id_generator", sequenceName = "block_parameters_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_parameter_id_generator")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_parameter_type")
    private ParameterType parameterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_block")
    private PageBlock pageBlock;

    @Column(name = "str_value", nullable = true)
    private String stringValue;

    @Column(name = "int_value", nullable = true)
    private Integer intValue;

    @Column(name = "data_value", nullable = true)
    private byte[] dataValue;

    @Column(name = "text_value", nullable = true)
    private String textValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public PageBlock getPageBlock() {
        return pageBlock;
    }

    public void setPageBlock(PageBlock pageBlock) {
        this.pageBlock = pageBlock;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public byte[] getDataValue() {
        return dataValue;
    }

    public void setDataValue(byte[] dataValue) {
        this.dataValue = dataValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
}
