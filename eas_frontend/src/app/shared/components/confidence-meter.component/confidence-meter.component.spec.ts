import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfidenceMeterComponent } from './confidence-meter.component';

describe('ConfidenceMeterComponent', () => {
  let component: ConfidenceMeterComponent;
  let fixture: ComponentFixture<ConfidenceMeterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfidenceMeterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfidenceMeterComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
