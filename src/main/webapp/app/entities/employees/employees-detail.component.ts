import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Employees } from './employees.model';
import { EmployeesService } from './employees.service';

@Component({
    selector: 'jhi-employees-detail',
    templateUrl: './employees-detail.component.html'
})
export class EmployeesDetailComponent implements OnInit, OnDestroy {

    employees: Employees;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private employeesService: EmployeesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmployees();
    }

    load(id) {
        this.employeesService.find(id).subscribe((employees) => {
            this.employees = employees;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmployees() {
        this.eventSubscriber = this.eventManager.subscribe(
            'employeesListModification',
            (response) => this.load(this.employees.id)
        );
    }
}
