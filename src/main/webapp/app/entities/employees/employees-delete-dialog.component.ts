import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Employees } from './employees.model';
import { EmployeesPopupService } from './employees-popup.service';
import { EmployeesService } from './employees.service';

@Component({
    selector: 'jhi-employees-delete-dialog',
    templateUrl: './employees-delete-dialog.component.html'
})
export class EmployeesDeleteDialogComponent {

    employees: Employees;

    constructor(
        private employeesService: EmployeesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.employeesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'employeesListModification',
                content: 'Deleted an employees'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-employees-delete-popup',
    template: ''
})
export class EmployeesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employeesPopupService: EmployeesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.employeesPopupService
                .open(EmployeesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
