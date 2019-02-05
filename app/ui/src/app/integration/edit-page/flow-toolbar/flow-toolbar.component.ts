import { Component, Input, ElementRef, ViewChild } from '@angular/core';
import { CurrentFlowService } from '../current-flow.service';
import { FlowPageService } from '../flow-page.service';
import { IntegrationType } from '@syndesis/ui/platform';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'syndesis-integration-flow-toolbar',
  templateUrl: './flow-toolbar.component.html',
  styleUrls: ['../../integration-common.scss', './flow-toolbar.component.scss']
})
export class FlowToolbarComponent {
  @Input()
  isApiProvider = false;
  @Input()
  hideOperationsButton = true;
  IntegrationType = IntegrationType;
  editingName = false;
  @ViewChild('nameInput') nameInput: ElementRef;

  constructor(
    public currentFlowService: CurrentFlowService,
    public flowPageService: FlowPageService,
    public route: ActivatedRoute
  ) {}

  get saveInProgress() {
    return this.flowPageService.saveInProgress;
  }

  get publishInProgress() {
    return this.flowPageService.publishInProgress;
  }

  get integrationName() {
    return (this.currentFlowService.integration || { name: '' }).name || '';
  }

  set integrationName(name: string) {
    this.currentFlowService.events.emit({
      kind: 'integration-set-property',
      property: 'name',
      value: name
    });
  }

  cancel() {
    this.flowPageService.cancel();
  }

  startEditingName() {
    this.editingName = true;
    this.nameInput.nativeElement.select();
  }

  stopEditingName() {
    this.editingName = false;
  }

  save(targetRoute) {
    this.flowPageService.save(
      this.route.firstChild,
      targetRoute || ['..', 'save-or-add-step']
    );
  }

  publish() {
    this.flowPageService.publish(this.route.firstChild);
  }

  get currentStep() {
    return this.flowPageService.getCurrentStep(this.route);
  }
}