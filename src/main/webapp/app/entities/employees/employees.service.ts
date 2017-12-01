import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Employees } from './employees.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmployeesService {

    private resourceUrl = SERVER_API_URL + 'api/employees';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employees';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(employees: Employees): Observable<Employees> {
        const copy = this.convert(employees);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(employees: Employees): Observable<Employees> {
        const copy = this.convert(employees);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Employees> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Employees.
     */
    private convertItemFromServer(json: any): Employees {
        const entity: Employees = Object.assign(new Employees(), json);
        entity.dob = this.dateUtils
            .convertLocalDateFromServer(json.dob);
        return entity;
    }

    /**
     * Convert a Employees to a JSON which can be sent to the server.
     */
    private convert(employees: Employees): Employees {
        const copy: Employees = Object.assign({}, employees);
        copy.dob = this.dateUtils
            .convertLocalDateToServer(employees.dob);
        return copy;
    }
}
