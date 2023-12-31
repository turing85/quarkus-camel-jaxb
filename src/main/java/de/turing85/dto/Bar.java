package de.turing85.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@RegisterForReflection(serialization = true)
@XmlType
@XmlRootElement(name = "Bar")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.MODULE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SuppressWarnings("unused")
public class Bar {
  private String name;
  private int number;

  @XmlElement(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlElement(name = "Number")
  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
