import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Employees } from './employees.model';
import { EmployeesPopupService } from './employees-popup.service';
import { EmployeesService } from './employees.service';

@Component({
    selector: 'jhi-employees-dialog',
    templateUrl: './employees-dialog.component.html'
})
export class EmployeesDialogComponent implements OnInit {

    employees: Employees;
    isSaving: boolean;
    dobDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private employeesService: EmployeesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.employees.id !== undefined) {
            this.subscribeToSaveResponse(
                this.employeesService.update(this.employees));
        } else {
            this.subscribeToSaveResponse(
                this.employeesService.create(this.employees));
        }
    }

    private subscribeToSaveResponse(result: Observable<Employees>) {
        result.subscribe((res: Employees) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Employees) {
        this.eventManager.broadcast({ name: 'employeesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-employees-popup',
    template: ''
})
export class EmployeesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employeesPopupService: EmployeesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.employeesPopupService
                    .open(EmployeesDialogComponent as Component, params['id']);
            } else {
                this.employeesPopupService
                    .open(EmployeesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
